package fr.jac.granarolo.wargame.classes;

import fr.jac.granarolo.wargame.services.HexService;
import fr.jac.granarolo.wargame.services.UnitService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {
    @Autowired
    static ApplicationContext context;

    @Autowired
    public UnitService unitService;

    @Autowired
    public HexService hexService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    public static ApplicationContext getApplicationContext() {
        return context;
    }
}