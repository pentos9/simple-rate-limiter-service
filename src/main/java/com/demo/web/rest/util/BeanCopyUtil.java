package com.demo.web.rest.util;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory.Builder;
import ma.glasnost.orika.metadata.TypeFactory;

import java.util.List;
import java.util.Set;

public class BeanCopyUtil {
    private static MapperFactory mapperFactory = (new Builder()).build();
    private static MapperFacade mapper;

    public BeanCopyUtil() {
    }

    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        return mapper.mapAsList(sourceList, destinationClass);
    }

    public static <S, D> Set<D> mapSet(Iterable<S> sourceList, Class<D> destinationClass) {
        return mapper.mapAsSet(sourceList, destinationClass);
    }

    public static <S, D> D[] mapArray(S[] sourceArray, Class<D> destinationClass) {
        D[] destinationArray = ArrayUtil.newArray(destinationClass, sourceArray.length);
        return mapper.mapAsArray(destinationArray, sourceArray, destinationClass);
    }

    static {
        mapperFactory.registerObjectFactory(new ObjectFactory<ObjectNode>() {
            public ObjectNode create(Object source, MappingContext mappingContext) {
                return (ObjectNode) JsonUtil.convertObj(source);
            }
        }, TypeFactory.valueOf(ObjectNode.class));
        mapperFactory.registerObjectFactory(new ObjectFactory<ArrayNode>() {
            public ArrayNode create(Object source, MappingContext mappingContext) {
                return (ArrayNode) JsonUtil.convertObj(source);
            }
        }, TypeFactory.valueOf(ArrayNode.class));
        mapper = mapperFactory.getMapperFacade();
    }
}
