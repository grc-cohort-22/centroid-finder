package io.github.konradkelly.centroidfinder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Caps how many jobs one caller can submit per window.
 *
 * <p>Authentication answers "who is calling" but not "how much". A single valid
 * credential can still queue 100 jobs, and each one launches a JVM that decodes
 * video — the expensive thing this service does. This bounds that.
 *
 * <p>Deliberately a fixed window held in memory rather than a token bucket in
 * Redis. The service runs as a single ECS task and the limit only needs to stop
 * accidental or casual abuse; a distributed limiter would be more machinery than
 * the threat justifies. The tradeoff is real and worth naming: counters reset on
 * restart, and the limit is per-instance, so it would need replacing before
 * running more than one replica.
 */
@Component
@ConfigurationProperties(prefix = "app.rate-limit")
public class JobSubmissionRateLimitFilter extends OncePerRequestFilter {

    private int maxSubmissions = 10;
    private Duration window = Duration.ofMinutes(1);

    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    public void setMaxSubmissions(int maxSubmissions) {
        this.maxSubmissions = maxSubmissions;
    }

    public void setWindow(Duration window) {
        this.window = window;
    }

    /** Only guards job submission; reads are unmetered. */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(HttpMethod.POST.matches(request.getMethod())
            && request.getRequestURI().startsWith("/process"));
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws ServletException, IOException {
        if (allow(callerKey(request), System.currentTimeMillis())) {
            chain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader("Retry-After", Long.toString(window.toSeconds()));
        response.setContentType("application/json");
        response.getWriter().write(
            "{\"error\":\"Too many job submissions. Limit is " + maxSubmissions
            + " per " + window.toSeconds() + "s.\"}"
        );
    }

    /**
     * Prefer the authenticated principal over the remote address: behind an ALB
     * every request shares a small set of source addresses, so limiting by IP
     * alone would throttle unrelated callers together.
     */
    private String callerKey(HttpServletRequest request) {
        String user = request.getRemoteUser();
        return user != null ? "user:" + user : "addr:" + request.getRemoteAddr();
    }

    /** Package-private so the unit test can drive time instead of sleeping. */
    boolean allow(String key, long nowMillis) {
        Window current = windows.compute(key, (ignored, existing) ->
            existing == null || nowMillis - existing.startedAtMillis >= window.toMillis()
                ? new Window(nowMillis)
                : existing);
        return current.count.incrementAndGet() <= maxSubmissions;
    }

    private static final class Window {
        private final long startedAtMillis;
        private final AtomicInteger count = new AtomicInteger();

        private Window(long startedAtMillis) {
            this.startedAtMillis = startedAtMillis;
        }
    }
}
