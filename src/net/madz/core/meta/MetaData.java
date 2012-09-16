package net.madz.core.meta;

import net.madz.core.common.DottedPath;
import net.madz.core.verification.VerificationFailureSet;

public interface MetaData {
    DottedPath getDottedPath();

    MetaData getParent();
    
    void verifyMetaData( VerificationFailureSet verificationSet );
}
