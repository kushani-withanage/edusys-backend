package com.edusys.util;

import com.edusys.enums.EntityPrefix;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
    
    public String generateId(EntityPrefix entityPrefix, long currentCount) {
        return String.format("%s%04d", entityPrefix.getPrefix(), currentCount + 1);
    }
}
