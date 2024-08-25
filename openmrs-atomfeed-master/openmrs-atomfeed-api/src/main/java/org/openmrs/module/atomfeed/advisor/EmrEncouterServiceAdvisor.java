package org.openmrs.module.atomfeed.advisor;

import org.aopalliance.aop.Advice;
import org.openmrs.module.atomfeed.advice.EncounterSaveAdvice;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;
import java.sql.SQLException;

public class EmrEncouterServiceAdvisor extends StaticMethodMatcherPointcutAdvisor implements Advisor {
    private static final String SAVE_METHOD = "save";

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return SAVE_METHOD.equals(method.getName());
    }

    @Override
    public Advice getAdvice() {
        try {
            return new EncounterSaveAdvice();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
