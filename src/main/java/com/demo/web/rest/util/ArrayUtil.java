package com.demo.web.rest.util;

import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ArrayUtil {
    public ArrayUtil() {
    }

    public static <T> T[] newArray(Class<T> type, int length) {
        return (T[]) ((Object[]) Array.newInstance(type, length));
    }

    public static <T> T[] toArray(Collection<T> col, Class<T> type) {
        return col.toArray((T[]) ((T[]) Array.newInstance(type, 0)));
    }

    private static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static <T> T[] shuffle(T[] array) {
        if (array != null && array.length > 1) {
            Random rand = new Random();
            return shuffle(array, rand);
        } else {
            return array;
        }
    }

    public static <T> T[] shuffle(T[] array, Random random) {
        if (array != null && array.length > 1 && random != null) {
            for (int i = array.length; i > 1; --i) {
                swap(array, i - 1, random.nextInt(i));
            }
        }

        return array;
    }

    public static <T> T[] concat(T element, T[] array) {
        return ObjectArrays.concat(element, array);
    }

    public static <T> T[] concat(T[] array, T element) {
        return ObjectArrays.concat(array, element);
    }

    public static <T> List<T> asList(T... a) {
        return Arrays.asList(a);
    }

    public static List<Integer> intAsList(int... backingArray) {
        return Ints.asList(backingArray);
    }

    public static List<Long> longAsList(long... backingArray) {
        return Longs.asList(backingArray);
    }

    public static List<Double> doubleAsList(double... backingArray) {
        return Doubles.asList(backingArray);
    }
}
