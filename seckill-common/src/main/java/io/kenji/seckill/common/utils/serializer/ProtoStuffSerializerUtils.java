package io.kenji.seckill.common.utils.serializer;


import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
public class ProtoStuffSerializerUtils {

    public static <T> List<T> deserializeList(byte[] paramArrayOfByte, Class<T> targetClass){
        if (paramArrayOfByte==null||paramArrayOfByte.length==0){
            throw new RuntimeException("Hit exception whilst deserialize object, paramArrayOfByte is null");
        }
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        List<T> result = null;
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(paramArrayOfByte),schema);
        }catch (IOException e){
            throw new RuntimeException("Hit exception whilst deserializing object",e);
        }
        return result;
    }
}
