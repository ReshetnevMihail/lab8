package by.bsuir.lab5.model;

public interface CallableWithReturnAndArguments<ReturnType, ArgsType> {
    ReturnType call(ArgsType ... args);
}
