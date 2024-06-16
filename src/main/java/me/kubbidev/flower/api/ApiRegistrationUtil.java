package me.kubbidev.flower.api;

import net.flower.api.Flower;
import net.flower.api.FlowerProvider;

import java.lang.reflect.Method;

public class ApiRegistrationUtil {
    private static final Method REGISTER;
    private static final Method UNREGISTER;
    static {
        try {
            REGISTER = FlowerProvider.class.getDeclaredMethod("register", Flower.class);
            REGISTER.setAccessible(true);

            UNREGISTER = FlowerProvider.class.getDeclaredMethod("unregister");
            UNREGISTER.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void registerProvider(Flower flowerApi) {
        try {
            REGISTER.invoke(null, flowerApi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterProvider() {
        try {
            UNREGISTER.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}