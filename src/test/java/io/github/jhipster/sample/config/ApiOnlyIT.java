package io.github.jhipster.sample.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.jhipster.sample.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The backend is an API server only. It does not host the Angular application, which lives in the
 * jhipster-sample-app-frontend repository.
 */
@AutoConfigureMockMvc
@IntegrationTest
class ApiOnlyIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void unmappedPathIsDeniedInsteadOfForwardingToIndexHtml() throws Exception {
        mockMvc.perform(get("/unmapped/path")).andExpect(status().isForbidden()).andExpect(forwardedUrl(null));
    }

    @Test
    void formerStaticFilePathsAreNotPublic() throws Exception {
        mockMvc.perform(get("/index.html")).andExpect(status().isUnauthorized());
    }
}
