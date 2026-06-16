package com.universite.apirest.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidationAspect {

    private static final Logger logger = LoggerFactory.getLogger(ValidationAspect.class);

    @Before("execution(* com.universite.apirest.service.EtudiantService.createEtudiant(..))")
    public void validateCreateEtudiant(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            logger.info("Validating etudiant data before creation: {}", args[0]);
        }
    }

    @Before("execution(* com.universite.apirest.service.EtudiantService.updateEtudiant(..))")
    public void validateUpdateEtudiant(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 1) {
            logger.info("Validating etudiant data before update for ID {}: {}", args[0], args[1]);
        }
    }
}
