package WebService.SpringApplicationContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext  implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        context=applicationContext;
    }

    //ALL THE BEANS CREATED BY THE SPRING FRAMEWORK ARE PRESENT IN THE APPLICATION CONTEXT. WE CAN GET ACCESS TO THOSE BEAN USING THE NAME IN GET BEAN METHODE

    public static Object getBean(String beanName){
        return context.getBean(beanName);

    }
}
