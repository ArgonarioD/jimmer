package org.babyfish.jimmer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import org.babyfish.jimmer.meta.PropId;
import org.babyfish.jimmer.runtime.ImmutableSpi;

class ImmutablePropertyWriter extends BeanPropertyWriter {

    private final PropId propId;

    public ImmutablePropertyWriter(BeanPropertyWriter base, PropId propId) {
        super(base);
        this.propId = propId;
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
        ImmutableSpi spi = (ImmutableSpi) bean;
        if (spi.__isLoaded(propId) && spi.__isVisible(propId)) {
            super.serializeAsField(bean, gen, prov);
        }
    }

    @Override
    public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
        ImmutableSpi spi = (ImmutableSpi) bean;
        if (spi.__isLoaded(propId) && spi.__isVisible(propId)) {
            super.serializeAsElement(bean, gen, prov);
        }
    }
}
