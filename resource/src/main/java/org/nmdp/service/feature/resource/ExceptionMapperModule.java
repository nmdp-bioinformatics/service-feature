package org.nmdp.service.feature.resource;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ExceptionMapperModule extends AbstractModule {
    @Override
    protected void configure() {
        // empty
    }

    @Provides
    UserInputExceptionMapper createUserInputExceptionMapper(){
        return new UserInputExceptionMapper();
    }

}
