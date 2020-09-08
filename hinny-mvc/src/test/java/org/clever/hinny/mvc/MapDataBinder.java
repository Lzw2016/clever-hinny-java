package org.clever.hinny.mvc;

import org.springframework.beans.AbstractPropertyAccessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/07 17:25 <br/>
 */
public class MapDataBinder extends WebDataBinder {

    public MapDataBinder(Map<String, Object> map) {
        super(map);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getTarget() {
        Object target = super.getTarget();
        Assert.notNull(target, "Target bean should never be null!");
        return (Map<String, Object>) target;
    }

    @Nonnull
    @Override
    protected ConfigurablePropertyAccessor getPropertyAccessor() {
        return new MapPropertyAccessor(getTarget(), super.getConversionService());
    }

    private static class MapPropertyAccessor extends AbstractPropertyAccessor {
        private final Map<String, Object> map;
        private final ConversionService conversionService;

        private MapPropertyAccessor(Map<String, Object> map, ConversionService conversionService) {
            this.map = map;
            this.conversionService = conversionService;
        }

        @Override
        public boolean isReadableProperty(@Nonnull String propertyName) {
            return true;
        }

        @Override
        public boolean isWritableProperty(@Nonnull String propertyName) {
            return map.containsKey(propertyName);
        }

        @Override
        public TypeDescriptor getPropertyTypeDescriptor(@Nonnull String propertyName) throws BeansException {
            return TypeDescriptor.forObject(map.get(propertyName));
        }

        @Override
        public Object getPropertyValue(@Nullable String propertyName) throws BeansException {
            return map.get(propertyName);
        }

        @Override
        public void setPropertyValue(@Nullable String propertyName, @Nullable Object value) throws BeansException {
            map.put(propertyName, value);
//            if (!isWritableProperty(propertyName)) {
//                throw new NotWritablePropertyException(String.class, propertyName);
//            }
//            PropertyPath leafProperty = getPropertyPath(propertyName).getLeafProperty();
//            TypeInformation<?> owningType = leafProperty.getOwningType();
//            TypeInformation<?> propertyType = leafProperty.getTypeInformation();
//            propertyType = propertyName.endsWith("]") ? propertyType.getActualType() : propertyType;
//            if (propertyType != null && conversionRequired(value, propertyType.getType())) {
//                PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(owningType.getType(), leafProperty.getSegment());
//                if (descriptor == null) {
//                    throw new IllegalStateException(String.format("Couldn't find PropertyDescriptor for %s on %s!", leafProperty.getSegment(), owningType.getType()));
//                }
//                MethodParameter methodParameter = new MethodParameter(descriptor.getReadMethod(), -1);
//                TypeDescriptor typeDescriptor = TypeDescriptor.nested(methodParameter, 0);
//                if (typeDescriptor == null) {
//                    throw new IllegalStateException(String.format("Couldn't obtain type descriptor for method parameter %s!", methodParameter));
//                }
//                value = conversionService.convert(value, TypeDescriptor.forObject(value), typeDescriptor);
//            }
//            EvaluationContext context = SimpleEvaluationContext
//                    .forPropertyAccessors(new MapPropertyAccessor.PropertyTraversingMapAccessor(type, conversionService))
//                    .withConversionService(conversionService)
//                    .withRootObject(map)
//                    .build();
//            Expression expression = PARSER.parseExpression(propertyName);
//            try {
//                expression.setValue(context, value);
//            } catch (SpelEvaluationException o_O) {
//                throw new NotWritablePropertyException(type, propertyName, "Could not write property!", o_O);
//            }
        }

//        private boolean conversionRequired(@Nullable Object source, Class<?> targetType) {
//            if (source == null || targetType.isInstance(source)) {
//                return false;
//            }
//            return conversionService.canConvert(source.getClass(), targetType);
//        }

//        private PropertyPath getPropertyPath(String propertyName) {
//            String plainPropertyPath = propertyName.replaceAll("\\[.*?\\]", "");
//            return PropertyPath.from(plainPropertyPath, type);
//        }
    }

//    private static final class PropertyTraversingMapAccessor extends MapAccessor {
//        private final ConversionService conversionService;
//        private Class<?> type;
//
//        public PropertyTraversingMapAccessor(Class<?> type, ConversionService conversionService) {
//            Assert.notNull(type, "Type must not be null!");
//            Assert.notNull(conversionService, "ConversionService must not be null!");
//            this.type = type;
//            this.conversionService = conversionService;
//        }
//
//        @Override
//        public boolean canRead(@Nullable EvaluationContext context, @Nullable Object target, @Nullable String name) throws AccessException {
//            return true;
//        }
//
//        @Override
//        @SuppressWarnings({"unchecked", "NullableProblems"})
//        public TypedValue read(@Nullable EvaluationContext context, @Nullable Object target, @Nullable String name) throws AccessException {
//            if (target == null) {
//                return TypedValue.NULL;
//            }
//            assert name != null;
//            PropertyPath path = PropertyPath.from(name, type);
//            try {
//                assert context != null;
//                return super.read(context, target, name);
//            } catch (AccessException o_O) {
//                Object emptyResult = path.isCollection() ? CollectionFactory.createCollection(List.class, 0) : CollectionFactory.createMap(Map.class, 0);
//                ((Map<String, Object>) target).put(name, emptyResult);
//                return new TypedValue(emptyResult, getDescriptor(path, emptyResult));
//            } finally {
//                this.type = path.getType();
//            }
//        }
//
//        private TypeDescriptor getDescriptor(PropertyPath path, Object emptyValue) {
//            Class<?> actualPropertyType = path.getType();
//            TypeDescriptor valueDescriptor = conversionService.canConvert(String.class, actualPropertyType)
//                    ? TypeDescriptor.valueOf(String.class)
//                    : TypeDescriptor.valueOf(HashMap.class);
//            return path.isCollection() ? TypeDescriptor.collection(emptyValue.getClass(), valueDescriptor)
//                    : TypeDescriptor.map(emptyValue.getClass(), TypeDescriptor.valueOf(String.class), valueDescriptor);
//        }
//    }
}
