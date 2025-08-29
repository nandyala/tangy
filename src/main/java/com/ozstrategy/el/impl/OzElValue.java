package com.ozstrategy.el.impl;

import java.io.Serializable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ozstrategy.el.exception.NotSupportException;
import com.ozstrategy.el.util.NoCaseHashMap;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/5/13 Time: 2:24 PM To change this template use File | Settings | File
 * Templates.
 *
 * @author Rojer Luo
 * @version $Revision$, $Date$
 */
public class OzElValue implements Serializable {
    //~ Static fields/initializers ---------------------------------------------------------------------------------------

    /**
     * Use serialVersionUID for interoperability.
     */
    private static final long serialVersionUID = -1175329068116852391L;

    //~ Enums ------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @author Rojer Luo
     * @version $Revision$, $Date$
     */
    enum SUPPORT_TYPE {
        //~ Enum constants -------------------------------------------------------------------------------------------------

        DECIMAL, LONG, INTEGER, BOOLEAN, STRING, DATE, MAP, OBJECT, NULL, LIST, SKIP, EXPRESSION, FUNCTION, UNKNOWN
    }

    //~ Instance fields --------------------------------------------------------------------------------------------------

    /**
     * Is this value allow to be cached.
     */
    protected boolean cacheable = true;

    /**
     * The class for the value.
     */
    protected Class clazz;

    /**
     * Default data format.
     */
    protected String dateFormat = "yyyy/MM/dd";

    /**
     * Element Class for Collection.
     */
    protected Class elementClazz;

    /**
     * Element Class for Collection.
     */
    protected Class keyClazz;

    /**
     * The name of the value.
     */
    protected String name;

    /**
     * The path of the value.
     */
    protected String path;

    /**
     * Boolean result.
     */
    protected boolean result = false;

    /**
     * The String value.
     */
    protected String strValue;

    /**
     * The type defined.
     */
    protected SUPPORT_TYPE type;

    /**
     * The Object value.
     */
    protected Object value;

    //~ Constructors -----------------------------------------------------------------------------------------------------

    /**
     * Creates a new OzElValue object.
     */
    public OzElValue() {
        name = "NULL";
        this.type = SUPPORT_TYPE.NULL;
        this.value = null;
        this.strValue = "<NULL>";
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param value DOCUMENT ME!
     */
    public OzElValue(Date value) {
        this.type = SUPPORT_TYPE.DATE;
        this.value = value;
        this.clazz = value.getClass();

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        this.strValue = sdf.format(value);
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param value DOCUMENT ME!
     */
    public OzElValue(Boolean value) {
        this.type = SUPPORT_TYPE.BOOLEAN;
        this.value = value;
        this.clazz = value.getClass();
        this.strValue = Boolean.TRUE.equals(value) ? "true" : "false";
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param value DOCUMENT ME!
     */
    public OzElValue(Long value) {
        this.type = SUPPORT_TYPE.LONG;
        this.value = value;
        this.clazz = value.getClass();
        this.strValue = value.toString();
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param value DOCUMENT ME!
     */
    public OzElValue(Integer value) {
        this.type = SUPPORT_TYPE.INTEGER;
        this.value = value;
        this.clazz = value.getClass();
        this.strValue = value.toString();
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param value DOCUMENT ME!
     */
    public OzElValue(Collection value) {
        this.type = SUPPORT_TYPE.LIST;

        List list = new ArrayList<Object>();

        for (Object object : value) {
            list.add(object);
        }

        this.value = list;
        this.clazz = value.getClass();
        this.strValue = this.value.toString();
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param value DOCUMENT ME!
     */
    public OzElValue(BigDecimal value) {
        this.type = SUPPORT_TYPE.DECIMAL;
        this.value = value;
        this.clazz = value.getClass();
        this.strValue = value.toPlainString();
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param value DOCUMENT ME!
     */
    public OzElValue(Map<String, Object> value) {
        this.type = SUPPORT_TYPE.MAP;

        NoCaseHashMap<Object> map = new NoCaseHashMap<Object>();
        map.putAll(value);
        this.value = map;

        this.clazz = (value == null) ? null : value.getClass();
        this.strValue = (value == null) ? "" : value.toString();
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param value DOCUMENT ME!
     */
    public OzElValue(Object value) {
        this.type = SUPPORT_TYPE.OBJECT;
        this.value = value;
        this.clazz = (value == null) ? null : value.getClass();
        this.strValue = (value == null) ? "" : value.toString();
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param value DOCUMENT ME!
     */
    public OzElValue(String value) {
        this.type = SUPPORT_TYPE.STRING;
        this.value = value;
        this.clazz = value.getClass();
        this.strValue = value;
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param type  DOCUMENT ME!
     * @param clazz DOCUMENT ME!
     */
    public OzElValue(SUPPORT_TYPE type, Class clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param name  DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public OzElValue(String name, Object value) {
        this.name = name;

        if (value != null) {
            this.value = value;
            this.clazz = value.getClass();

            if (value instanceof Class) {
                this.type = SUPPORT_TYPE.FUNCTION;
                this.strValue = ((Class) value).getName();
            } else if (value instanceof Integer) {
                this.type = SUPPORT_TYPE.INTEGER;
                this.strValue = value.toString();
            } else if (value instanceof Long) {
                this.type = SUPPORT_TYPE.LONG;
                this.strValue = value.toString();
            } else if (value instanceof BigDecimal) {
                this.type = SUPPORT_TYPE.DECIMAL;
                this.strValue = ((BigDecimal) value).toPlainString();
            } else if (value instanceof Boolean) {
                this.type = SUPPORT_TYPE.BOOLEAN;
                this.strValue = Boolean.TRUE.equals(value) ? "true" : "false";
            } else if (value instanceof String) {
                this.type = SUPPORT_TYPE.STRING;
                this.strValue = (String) value;
            } else if (value instanceof Date) {
                this.type = SUPPORT_TYPE.DATE;

                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                this.strValue = sdf.format(value);
            } else if (value instanceof Map) {
                this.type = SUPPORT_TYPE.MAP;

                NoCaseHashMap<Object> map = new NoCaseHashMap<Object>();
                map.putAll((Map) value);
                this.value = map;

                this.strValue = value.toString();
            } else if (value instanceof List) {
                this.type = SUPPORT_TYPE.LIST;
                this.strValue = value.toString();
            } else {
                this.type = SUPPORT_TYPE.OBJECT;
                this.strValue = value.toString();
            } // end if-else
        } else {
            this.type = SUPPORT_TYPE.NULL;
            this.value = null;
        }   // end if-else
    }     // end ctor OzElValue

    /**
     * Creates a new OzElValue object.
     *
     * @param name  String
     * @param type  SUPPORT_TYPE
     * @param clazz Class
     */
    public OzElValue(String name, SUPPORT_TYPE type, Class clazz) {
        this.name = name;
        this.type = type;
        this.clazz = clazz;
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param type     DOCUMENT ME!
     * @param name     DOCUMENT ME!
     * @param strValue DOCUMENT ME!
     */
    public OzElValue(SUPPORT_TYPE type, String name, String strValue) {
        this.name = name;
        this.strValue = strValue;
        this.type = type;

        switch (type) {
            case BOOLEAN: {
                if ("TRUE".equalsIgnoreCase(strValue)
                        || "T".equalsIgnoreCase(strValue)
                        || "YES".equalsIgnoreCase(strValue)
                        || "Y".equalsIgnoreCase(strValue)) {
                    value = Boolean.TRUE;
                } else if ("FALSE".equalsIgnoreCase(strValue)
                        || "F".equalsIgnoreCase(strValue)
                        || "NO".equalsIgnoreCase(strValue)
                        || "N".equalsIgnoreCase(strValue)) {
                    value = Boolean.FALSE;
                } else {
                    value = null;
                }

                break;
            }

            case STRING: {
                value = strValue;

                break;
            }

            case DATE: {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                    value = sdf.parse(strValue);
                } catch (ParseException e) {
                    value = null;
                }

                break;
            }

            case INTEGER: {
                value = Integer.valueOf(strValue);

                break;
            }

            case LONG: {
                value = Long.valueOf(strValue);

                break;
            }

            case DECIMAL: {
                value = new BigDecimal(strValue);

                break;
            }

            case EXPRESSION: {
                value = strValue;

                break;
            }

            case FUNCTION: {
                return;
            }

            case SKIP: {
                value = null;

                return;
            }
        } // end switch

        if (value != null) {
            this.clazz = value.getClass();
        }

        if (value == null) {
            this.name = null;
            this.type = null;
            this.strValue = null;
        }
    } // end ctor OzElValue

    /**
     * Creates a new OzElValue object.
     *
     * @param name         String
     * @param type         SUPPORT_TYPE
     * @param clazz        Class
     * @param elementClazz Class
     */
    public OzElValue(String name, SUPPORT_TYPE type, Class clazz, Class elementClazz) {
        this.name = name;
        this.type = type;
        this.clazz = clazz;
        this.elementClazz = elementClazz;
    }

    /**
     * Creates a new OzElValue object.
     *
     * @param name       String
     * @param type       SUPPORT_TYPE
     * @param clazz      Class
     * @param keyClazz   Class
     * @param valueClazz Class
     */
    public OzElValue(String name, SUPPORT_TYPE type, Class clazz, Class keyClazz, Class valueClazz) {
        this.name = name;
        this.type = type;
        this.clazz = clazz;
        this.keyClazz = keyClazz;
        this.elementClazz = valueClazz;
    }

    //~ Methods ----------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param name       DOCUMENT ME!
     * @param expression DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static OzElValue createExpression(String name, String expression) {
        if ((name == null) || (expression == null)) {
            return null;
        }

        return new OzElValue(SUPPORT_TYPE.EXPRESSION, name, expression);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param name       DOCUMENT ME!
     * @param expression DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static OzElValue createFunction(String name, String expression) {
        if ((name == null) || (expression == null)) {
            return null;
        }

        return new OzElValue(SUPPORT_TYPE.FUNCTION, name, expression);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param name String
     * @param type DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static OzElValue createOzElType(String name, Type type) {
        if (type == null) {
            return new OzElValue(name, SUPPORT_TYPE.UNKNOWN, null);
        } else if (type.equals(Date.class)) {
            return new OzElValue(name, SUPPORT_TYPE.DATE, (Class) type);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return new OzElValue(name, SUPPORT_TYPE.BOOLEAN, (Class) type);
        } else if (type.equals(String.class)) {
            return new OzElValue(name, SUPPORT_TYPE.STRING, (Class) type);
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return new OzElValue(name, SUPPORT_TYPE.INTEGER, (Class) type);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return new OzElValue(name, SUPPORT_TYPE.LONG, (Class) type);
        } else if (type.equals(BigDecimal.class)
                || type.equals(BigInteger.class)
                || type.equals(double.class)
                || type.equals(float.class)) {
            return new OzElValue(name, SUPPORT_TYPE.DECIMAL, (Class) type);
        } else if (type instanceof ParameterizedType) {
            Class collectClass = (Class) ((ParameterizedType) type).getRawType();
            if (Map.class.isAssignableFrom(collectClass)) {
                Class clsKey = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
                Class clsElement = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
                return new OzElValue(null, SUPPORT_TYPE.MAP, collectClass, clsKey, clsElement);
            } else {
                Class clsElement = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
                return new OzElValue(null, SUPPORT_TYPE.LIST, collectClass, clsElement);
            }
        } else if (Map.class.isAssignableFrom((Class) type)) {
            return new OzElValue(name, SUPPORT_TYPE.MAP, (Class) type);
        } else if (Collection.class.isAssignableFrom((Class) type)) {
            return new OzElValue(name, SUPPORT_TYPE.LIST, (Class) type);
        } else {
            return new OzElValue(name, SUPPORT_TYPE.OBJECT, (Class) type);
        }
    } // end method createOzElType

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws NotSupportException DOCUMENT ME!
     */
    public static OzElValue createOzElValue(Object value) {
        if (value == null) {
            return new OzElValue();
        }

        if (value instanceof Date) {
            return new OzElValue((Date) value);
        } else if (value instanceof Boolean) {
            return new OzElValue((Boolean) value);
        } else if (value instanceof String) {
            return new OzElValue((String) value);
        } else if (value instanceof Integer) {
            return new OzElValue((Integer) value);
        } else if (value instanceof Long) {
            return new OzElValue((Long) value);
        } else if (value instanceof BigDecimal) {
            return new OzElValue((BigDecimal) value);
        } else {
            return new OzElValue(value);
        }

// throw new NotSupportException("Value type is not supported.");
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * Set the flag for cacheable.
     *
     * @param cacheable boolean
     * @return OzElValue
     */
    public OzElValue allowCache(boolean cacheable) {
        this.cacheable = cacheable;

        return this;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        OzElValue ozElValue = (OzElValue) o;

        if (!name.equals(ozElValue.name)) {
            return false;
        }

        if (!strValue.equals(ozElValue.strValue)) {
            return false;
        }

        if (!type.equals(ozElValue.type)) {
            return false;
        }

        return true;
    } // end method equals

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean getBooleanValue() {
        if (isBooleanValue()) {
            return (Boolean) value;
        } else {
            return false;
        }
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getClazz() {
        return clazz;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDateValue() {
        if (isDateValue()) {
            return (Date) value;
        } else {
            return null;
        }
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BigDecimal getDecimalValue() {
        if (isDecimalValue()) {
            return (BigDecimal) value;
        } else if (isLongValue()) {
            return new BigDecimal((Long) value);
        } else if (isIntegerValue()) {
            return new BigDecimal((Integer) value);
        } else {
            return null;
        }
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * getter method for key clazz.
     *
     * @return Class
     */
    public Class getKeyClazz() {
        return keyClazz;
    }

    /**
     * getter method for element clazz.
     *
     * @return Class
     */
    public Class getElementClazz() {
        return elementClazz;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer getIntegerValue() {
        if (isIntegerValue()) {
            return (Integer) value;
        } else {
            return null;
        }
    }

    //~ ------------------------------------------------------------------------------------------------------------------
    public Map<String, Object> getMapValue() {
        return (Map<String, Object>) value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<OzElValue> getListValue() {
        return (List<OzElValue>) value;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getListValueInArray() {
        List<OzElValue> list = (List<OzElValue>) value;
        int size = list.size();
        Object[] retObjs = new Object[size];

        for (int i = 0; i < size; i++) {
            retObjs[i] = list.get(i).getValue();
        }

        return retObjs;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long getLongValue() {
        if (isLongValue()) {
            return (Long) value;
        } else if (isIntegerValue()) {
            return Long.valueOf((Integer) value);
        } else {
            return null;
        }
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * Get Full path or just name.
     *
     * @return String
     */
    public String getPathOrName() {
        if (path != null) {
            return path;
        } else {
            return name;
        }
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStringValue() {
        if (isStringValue()) {
            return (String) value;
        } else {
            return null;
        }
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStrValue() {
        return strValue;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SUPPORT_TYPE getType() {
        return type;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return value;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getValueType() {
        switch (this.type) {
            case DECIMAL: {
                return BigDecimal.class;
            }

            case LONG: {
                return Long.class;
            }

            case INTEGER: {
                return Integer.class;
            }

            case BOOLEAN: {
                return Boolean.class;
            }

            case STRING: {
                return String.class;
            }

            case DATE: {
                return Date.class;
            }

            default: {
                return null;
            }
        } // end switch
    }   // end method getValueType

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = (31 * result) + strValue.hashCode();
        result = (31 * result) + type.hashCode();

        return result;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasName() {
        return ((name != null) && (!name.isEmpty()));
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBooleanValue() {
        return (type == SUPPORT_TYPE.BOOLEAN);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * getter method for cacheable.
     *
     * @return boolean
     */
    public boolean isCacheable() {
        return cacheable;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDateValue() {
        return (type == SUPPORT_TYPE.DATE);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDecimalCompatibleValue() {
        return ((type == SUPPORT_TYPE.DECIMAL) || (type == SUPPORT_TYPE.LONG) || (type == SUPPORT_TYPE.INTEGER));
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDecimalValue() {
        return (type == SUPPORT_TYPE.DECIMAL);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExpressionValue() {
        return (type == SUPPORT_TYPE.EXPRESSION);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFunctionValue() {
        return (type == SUPPORT_TYPE.FUNCTION);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isIntegerValue() {
        return (type == SUPPORT_TYPE.INTEGER);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isListValue() {
        return (type == SUPPORT_TYPE.LIST);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLongCompatibleValue() {
        return ((type == SUPPORT_TYPE.LONG) || (type == SUPPORT_TYPE.INTEGER));
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLongValue() {
        return (type == SUPPORT_TYPE.LONG);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMapValue() {
        return (type == SUPPORT_TYPE.MAP);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNullValue() {
        return (type == SUPPORT_TYPE.NULL);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isObjectValue() {
        return (type == SUPPORT_TYPE.OBJECT);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isResult() {
        return result;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSkipValue() {
        return (type == SUPPORT_TYPE.SKIP);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isStringValue() {
        return (type == SUPPORT_TYPE.STRING);
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * setter method for cacheable.
     *
     * @param cacheable boolean
     */
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * Set Path for the name.
     *
     * @param path $param.type$
     * @return set Path for the name.
     */
    public OzElValue setPath(String path) {
        this.path = path;

        return this;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param result DOCUMENT ME!
     * @return dOCUMENT ME!
     */
    public OzElValue setResult(boolean result) {
        this.result = result;

        return this;
    }

    //~ ------------------------------------------------------------------------------------------------------------------

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OzElValue{");
        sb.append("name='").append(name).append('\'');
        sb.append(", strValue='").append(strValue).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');

        return sb.toString();
    }
} // end class OzElValue
