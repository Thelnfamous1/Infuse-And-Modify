package com.infamous.simply_harder.custom;

import java.util.function.Supplier;

public class SimpleRegistryObjectHolder<T> implements Supplier<T> {
    private T heldObject;
    private boolean isRegistered;

    public void register(T objectToHold){
        if(this.isRegistered){
            throw new IllegalStateException("Cannot register an already registered object!");
        } else{
            this.heldObject = objectToHold;
            this.isRegistered = true;
        }
    }

    @Override
    public T get() {
        if(this.isRegistered) {
            return this.heldObject;
        } else{
            throw new IllegalStateException("Cannot retrieve an unregistered object!");
        }
    }
}
