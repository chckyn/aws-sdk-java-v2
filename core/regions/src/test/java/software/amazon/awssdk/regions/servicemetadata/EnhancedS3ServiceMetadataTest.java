/*
 * Copyright 2010-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.regions.servicemetadata;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.profiles.ProfileFileSystemSetting;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.testutils.EnvironmentVariableHelper;

public class EnhancedS3ServiceMetadataTest {
    private static final EnvironmentVariableHelper ENVIRONMENT_VARIABLE_HELPER = new EnvironmentVariableHelper();
    private static final URI S3_GLOBAL_ENDPOINT = URI.create("s3.amazonaws.com");
    private static final URI S3_IAD_REGIONAL_ENDPOINT = URI.create("s3.us-east-1.amazonaws.com");

    private EnhancedS3ServiceMetadata enchancedMetadata = new EnhancedS3ServiceMetadata();

    @After
    public void methodSetup() {
        ENVIRONMENT_VARIABLE_HELPER.reset();
        System.clearProperty(ProfileFileSystemSetting.AWS_PROFILE.property());
        System.clearProperty(ProfileFileSystemSetting.AWS_CONFIG_FILE.property());

        enchancedMetadata = new EnhancedS3ServiceMetadata();
    }

    @Test
    public void optionNotSet_returnsGlobalEndpoint() {
        assertThat(enchancedMetadata.endpointFor(Region.US_EAST_1)).isEqualTo(S3_GLOBAL_ENDPOINT);
    }

    @Test
    // TODO: Enable this once the endpoints.json file is updated with the regional endpoint
    @Ignore("Requires endpoints.json update")
    public void regionalSet_profile_returnsRegionalEndpoint() throws URISyntaxException {
        String testFile = "/profileconfig/s3_regional_config_profile.tst";

        System.setProperty(ProfileFileSystemSetting.AWS_PROFILE.property(), "regional_s3_endpoint");
        System.setProperty(ProfileFileSystemSetting.AWS_CONFIG_FILE.property(), Paths.get(getClass().getResource(testFile).toURI()).toString());

        assertThat(enchancedMetadata.endpointFor(Region.US_EAST_1)).isEqualTo(S3_IAD_REGIONAL_ENDPOINT);
    }

    @Test
    // TODO: Enable this once the endpoints.json file is updated with the regional endpoint
    @Ignore("Requires endpoints.json update")
    public void regionalSet_env_returnsRegionalEndpoint() {
        ENVIRONMENT_VARIABLE_HELPER.set(SdkSystemSetting.AWS_S3_US_EAST_1_REGIONAL_ENDPOINT.environmentVariable(), "regional");
        assertThat(enchancedMetadata.endpointFor(Region.US_EAST_1)).isEqualTo(S3_IAD_REGIONAL_ENDPOINT);
    }

    @Test
    // TODO: Enable this once the endpoints.json file is updated with the regional endpoint
    @Ignore("Requires endpoints.json update")
    public void regionalSet_mixedCase_env_returnsRegionalEndpoint() {
        ENVIRONMENT_VARIABLE_HELPER.set(SdkSystemSetting.AWS_S3_US_EAST_1_REGIONAL_ENDPOINT.environmentVariable(), "rEgIoNaL");
        assertThat(enchancedMetadata.endpointFor(Region.US_EAST_1)).isEqualTo(S3_IAD_REGIONAL_ENDPOINT);
    }

    @Test
    public void global_env_returnsGlobalEndpoint() {
        ENVIRONMENT_VARIABLE_HELPER.set(SdkSystemSetting.AWS_S3_US_EAST_1_REGIONAL_ENDPOINT.environmentVariable(), "non_regional");
        assertThat(enchancedMetadata.endpointFor(Region.US_EAST_1)).isEqualTo(S3_GLOBAL_ENDPOINT);
    }

    @Test
    public void valueNotEqualToRegional_env_returnsGlobalEndpoint() {
        ENVIRONMENT_VARIABLE_HELPER.set(SdkSystemSetting.AWS_S3_US_EAST_1_REGIONAL_ENDPOINT.environmentVariable(), "some-nonsense-value");
        assertThat(enchancedMetadata.endpointFor(Region.US_EAST_1)).isEqualTo(S3_GLOBAL_ENDPOINT);
    }
}
