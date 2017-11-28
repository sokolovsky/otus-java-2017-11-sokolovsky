package ru.otus.sokolovsky.hw2.execution;

public class ObjectDataGenerator extends AbstractDataGenerator {
    private Class concreteClass;
    private ObjectCreator creator;
    private Object[] data;

    ObjectDataGenerator(ObjectCreator creator) {
        this.creator = creator;
        concreteClass = creator.getCreatedClass();
    }

    @Override
    public void createData() {
        int count = count();
        if (count == 0){
            return;
        }

        data = new Object[count];
        for (int i = 0; i < count; i++) {
            data[i] = creator.create();
        }
    }

    @Override
    public String description() {
        return String.format(
                    "Creation %d objects of class '%s', %s",
                    count(),
                    concreteClass.getCanonicalName(),
                    creator.description() != null ? creator.description() : ""
                );
    }
}
