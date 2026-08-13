package io.github.konradkelly.centroidfinder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Verifies that authentication is scoped to job submission and nothing else.
 * Filters stay enabled here, unlike {@link VideoControllerTest}, precisely
 * because the filter chain is what is under test.
 */
@WebMvcTest(VideoController.class)
@Import({SecurityConfig.class, SecurityConfigTest.TestConfig.class})
class SecurityConfigTest {

    private static final String USER = "dev";
    private static final String PASSWORD = "dev";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoCatalogService videoCatalogService;

    @MockBean
    private ThumbnailService thumbnailService;

    @MockBean
    private JobService jobService;

    @MockBean
    private JobStatusMapper jobStatusMapper;

    /**
     * StaticFileConfig is a WebMvcConfigurer, so @WebMvcTest instantiates it and
     * it needs ServerPathsProperties. Same shim VideoControllerTest uses.
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        ServerPathsProperties serverPathsProperties() {
            return new ServerPathsProperties(
                java.nio.file.Path.of("videos"),
                java.nio.file.Path.of("results"),
                java.nio.file.Path.of("processor.jar"),
                java.time.Duration.ofMinutes(10)
            );
        }
    }

    @Test
    @DisplayName("listing videos is public — the browser frontend reads it on load")
    void listingVideosIsPublic() throws Exception {
        when(videoCatalogService.listVideos()).thenReturn(List.of("clip.mp4"));
        mockMvc.perform(get("/api/videos")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("job status is public — polling must not require a credential")
    void jobStatusIsPublic() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(jobService.get(eq(jobId))).thenReturn(null);
        mockMvc.perform(get("/process/" + jobId + "/status"))
            .andExpect(status().is(not(401)));
    }

    @Test
    @DisplayName("submitting a job without credentials is rejected")
    void submitRequiresAuthentication() throws Exception {
        mockMvc.perform(post("/process/clip.mp4").param("targetColor", "FF0000").param("threshold", "100"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("submitting a job with the wrong password is rejected")
    void submitRejectsBadCredentials() throws Exception {
        mockMvc.perform(post("/process/clip.mp4")
                .param("targetColor", "FF0000")
                .param("threshold", "100")
                .with(httpBasic(USER, "wrong-password")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("submitting a job with valid credentials is allowed through")
    void submitAcceptsValidCredentials() throws Exception {
        when(jobService.start(any(), any(), any())).thenReturn(UUID.randomUUID());
        mockMvc.perform(post("/process/clip.mp4")
                .param("targetColor", "FF0000")
                .param("threshold", "100")
                .with(httpBasic(USER, PASSWORD)))
            .andExpect(status().is(not(401)));
    }

    /** Small readability helper: assert a status is anything but the given code. */
    private static org.hamcrest.Matcher<Integer> not(int code) {
        return org.hamcrest.Matchers.not(org.hamcrest.Matchers.is(code));
    }
}
