package io.github.konradkelly.centroidfinder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Drives the counter directly with an explicit clock rather than sleeping, so
 * the window-expiry case is exercised without making the suite slow.
 */
class JobSubmissionRateLimitFilterTest {

    private JobSubmissionRateLimitFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JobSubmissionRateLimitFilter();
        filter.setMaxSubmissions(3);
        filter.setWindow(Duration.ofMinutes(1));
    }

    @Test
    @DisplayName("allows submissions up to the limit, then refuses")
    void allowsUpToLimit() {
        long now = 1_000L;
        assertTrue(filter.allow("user:dev", now));
        assertTrue(filter.allow("user:dev", now));
        assertTrue(filter.allow("user:dev", now));
        assertFalse(filter.allow("user:dev", now), "fourth submission in the window should be refused");
    }

    @Test
    @DisplayName("starts a fresh window once the old one has elapsed")
    void resetsAfterWindow() {
        long start = 1_000L;
        for (int i = 0; i < 3; i++) {
            filter.allow("user:dev", start);
        }
        assertFalse(filter.allow("user:dev", start));

        long afterWindow = start + Duration.ofMinutes(1).toMillis();
        assertTrue(filter.allow("user:dev", afterWindow), "a new window should reset the count");
    }

    @Test
    @DisplayName("meters each caller separately")
    void callersAreIndependent() {
        long now = 1_000L;
        for (int i = 0; i < 3; i++) {
            filter.allow("user:alice", now);
        }
        assertFalse(filter.allow("user:alice", now));
        assertTrue(filter.allow("user:bob", now), "bob's budget is his own");
    }

    @Test
    @DisplayName("does not meter anything other than job submission")
    void onlyGuardsJobSubmission() {
        assertTrue(filter.shouldNotFilter(request("GET", "/api/videos")));
        assertTrue(filter.shouldNotFilter(request("GET", "/process/abc/status")));
        assertTrue(filter.shouldNotFilter(request("GET", "/results/out.csv")));
        assertFalse(filter.shouldNotFilter(request("POST", "/process/clip.mp4")));
    }

    private static jakarta.servlet.http.HttpServletRequest request(String method, String uri) {
        org.springframework.mock.web.MockHttpServletRequest mock =
            new org.springframework.mock.web.MockHttpServletRequest(method, uri);
        mock.setRequestURI(uri);
        return mock;
    }
}
