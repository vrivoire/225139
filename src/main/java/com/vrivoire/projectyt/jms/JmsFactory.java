package com.vrivoire.projectyt.jms;

public class JmsFactory {

    private JmsConsumer jmsConsumer;
    private JmsProducer jmsProducer;

    private JmsFactory() {
    }

    public static JmsFactory getInstance() {
        return NewSingletonHolder.INSTANCE;
    }

    public JmsConsumer getJmsConsumer() throws Exception {
        if (jmsConsumer == null) {
            jmsConsumer = (JmsConsumer) getImplFromInterface(JmsConsumer.class);
        }
        return jmsConsumer;
    }

    public JmsProducer getJmsProducer() throws Exception {
        if (jmsProducer == null) {
            jmsProducer = (JmsProducer) getImplFromInterface(JmsProducer.class);
        }
        return jmsProducer;
    }

    private Object getImplFromInterface(Class<?> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String nameImpl = clazz.getPackage().getName() + ".impl." + clazz.getSimpleName() + "Impl";
        Class<?> forName = Class.forName(nameImpl);
        Object newInstance = forName.newInstance();
        return newInstance;
    }

    private static class NewSingletonHolder {

        private static final JmsFactory INSTANCE = new JmsFactory();
    }
}
