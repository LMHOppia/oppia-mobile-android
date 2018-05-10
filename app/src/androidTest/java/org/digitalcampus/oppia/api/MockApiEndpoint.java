package org.digitalcampus.oppia.api;

import android.content.Context;
import okhttp3.mockwebserver.MockWebServer;

public class MockApiEndpoint implements ApiEndpoint {
    private MockWebServer mockServer;

    public MockApiEndpoint(MockWebServer mockServer){
        this.mockServer = mockServer;
    }

    @Override
    public String getFullURL(Context ctx, String apiPath) {
        return mockServer.url(apiPath).toString();
    }
}