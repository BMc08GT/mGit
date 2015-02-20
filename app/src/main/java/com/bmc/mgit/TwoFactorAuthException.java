package com.bmc.mgit;

import java.io.IOException;

public class TwoFactorAuthException extends IOException {
    private String mTwoFactorAuthType;

    public TwoFactorAuthException(IOException e, String twoFactorAuthType) {
        this.mTwoFactorAuthType = twoFactorAuthType;
    }

    public String getTwoFactorAuthType() {
        return mTwoFactorAuthType;
    }
}
