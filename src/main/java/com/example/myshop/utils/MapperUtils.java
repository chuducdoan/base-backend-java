package com.example.myshop.utils;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor
public class MapperUtils {

    public static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static <E, D> D toDto (E entity, Class<D> dto) {
        return MODEL_MAPPER.map(entity, dto);
    }

    public static <D, E> E toEntity (D dto, Class<E> entity) {
        return MODEL_MAPPER.map(dto, entity);
    }
}
