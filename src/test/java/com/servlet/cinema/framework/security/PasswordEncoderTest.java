package com.servlet.cinema.framework.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderTest {

    @Test
    void encodeSHA256() {
        assertThat(PasswordEncoder.encode("Lorem ipsum")).isEqualTo("ﾩﾦix￳xElﾁﾏﾸﾣ￧ￆﾭ=,ﾃ￦'$ￌﾽ￪{6%?ﾸ￟^￝");
    }
}