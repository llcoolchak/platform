/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package org.wso2.carbon.bam.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.thrift.*;
import org.apache.thrift.async.*;
import org.apache.thrift.meta_data.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.*;

public class AuthenticatorService {

  public interface Iface {

    public String authenticate(String userName, String password) throws AuthenticationException,
                                                                        TException;

  }

  public interface AsyncIface {

    public void authenticate(String userName, String password, AsyncMethodCallback<AsyncClient.authenticate_call> resultHandler) throws TException;

  }

  public static class Client implements TServiceClient, Iface {
    public static class Factory implements TServiceClientFactory<Client> {
      public Factory() {}
      public Client getClient(TProtocol prot) {
        return new Client(prot);
      }
      public Client getClient(TProtocol iprot, TProtocol oprot) {
        return new Client(iprot, oprot);
      }
    }

    public Client(TProtocol prot)
    {
      this(prot, prot);
    }

    public Client(TProtocol iprot, TProtocol oprot)
    {
      iprot_ = iprot;
      oprot_ = oprot;
    }

    protected TProtocol iprot_;
    protected TProtocol oprot_;

    protected int seqid_;

    public TProtocol getInputProtocol()
    {
      return this.iprot_;
    }

    public TProtocol getOutputProtocol()
    {
      return this.oprot_;
    }

    public String authenticate(String userName, String password) throws AuthenticationException, TException
    {
      send_authenticate(userName, password);
      return recv_authenticate();
    }

    public void send_authenticate(String userName, String password) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("authenticate", TMessageType.CALL, ++seqid_));
      authenticate_args args = new authenticate_args();
      args.setUserName(userName);
      args.setPassword(password);
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public String recv_authenticate() throws AuthenticationException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      if (msg.seqid != seqid_) {
        throw new TApplicationException(TApplicationException.BAD_SEQUENCE_ID, "authenticate failed: out of sequence response");
      }
      authenticate_result result = new authenticate_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ae != null) {
        throw result.ae;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "authenticate failed: unknown result");
    }

  }
  public static class AsyncClient extends TAsyncClient implements AsyncIface {
    public static class Factory implements TAsyncClientFactory<AsyncClient> {
      private TAsyncClientManager clientManager;
      private TProtocolFactory protocolFactory;
      public Factory(TAsyncClientManager clientManager, TProtocolFactory protocolFactory) {
        this.clientManager = clientManager;
        this.protocolFactory = protocolFactory;
      }
      public AsyncClient getAsyncClient(TNonblockingTransport transport) {
        return new AsyncClient(protocolFactory, clientManager, transport);
      }
    }

    public AsyncClient(TProtocolFactory protocolFactory, TAsyncClientManager clientManager, TNonblockingTransport transport) {
      super(protocolFactory, clientManager, transport);
    }

    public void authenticate(String userName, String password, AsyncMethodCallback<authenticate_call> resultHandler) throws TException {
      checkReady();
      authenticate_call method_call = new authenticate_call(userName, password, resultHandler, this, protocolFactory, transport);
      manager.call(method_call);
    }

    public static class authenticate_call extends TAsyncMethodCall {
      private String userName;
      private String password;
      public authenticate_call(String userName, String password, AsyncMethodCallback<authenticate_call> resultHandler, TAsyncClient client, TProtocolFactory protocolFactory, TNonblockingTransport transport) throws TException {
        super(client, protocolFactory, transport, resultHandler, false);
        this.userName = userName;
        this.password = password;
      }

      public void write_args(TProtocol prot) throws TException {
        prot.writeMessageBegin(new TMessage("authenticate", TMessageType.CALL, 0));
        authenticate_args args = new authenticate_args();
        args.setUserName(userName);
        args.setPassword(password);
        args.write(prot);
        prot.writeMessageEnd();
      }

      public String getResult() throws AuthenticationException, TException {
        if (getState() != State.RESPONSE_READ) {
          throw new IllegalStateException("Method call not finished!");
        }
        TMemoryInputTransport memoryTransport = new TMemoryInputTransport(getFrameBuffer().array());
        TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
        return (new Client(prot)).recv_authenticate();
      }
    }

  }

  public static class Processor implements TProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
    public Processor(Iface iface)
    {
      iface_ = iface;
      processMap_.put("authenticate", new authenticate());
    }

    protected static interface ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException;
    }

    private Iface iface_;
    protected final HashMap<String,ProcessFunction> processMap_ = new HashMap<String,ProcessFunction>();

    public boolean process(TProtocol iprot, TProtocol oprot) throws TException
    {
      TMessage msg = iprot.readMessageBegin();
      ProcessFunction fn = processMap_.get(msg.name);
      if (fn == null) {
        TProtocolUtil.skip(iprot, TType.STRUCT);
        iprot.readMessageEnd();
        TApplicationException x = new TApplicationException(TApplicationException.UNKNOWN_METHOD, "Invalid method name: '"+msg.name+"'");
        oprot.writeMessageBegin(new TMessage(msg.name, TMessageType.EXCEPTION, msg.seqid));
        x.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
        return true;
      }
      fn.process(msg.seqid, iprot, oprot);
      return true;
    }

    private class authenticate implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        authenticate_args args = new authenticate_args();
        try {
          args.read(iprot);
        } catch (TProtocolException e) {
          iprot.readMessageEnd();
          TApplicationException x = new TApplicationException(TApplicationException.PROTOCOL_ERROR, e.getMessage());
          oprot.writeMessageBegin(new TMessage("authenticate", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        iprot.readMessageEnd();
        authenticate_result result = new authenticate_result();
        try {
          result.success = iface_.authenticate(args.userName, args.password);
        } catch (AuthenticationException ae) {
          result.ae = ae;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing authenticate", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing authenticate");
          oprot.writeMessageBegin(new TMessage("authenticate", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("authenticate", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

  }

  public static class authenticate_args implements TBase<authenticate_args, authenticate_args._Fields>, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("authenticate_args");

    private static final TField USER_NAME_FIELD_DESC = new TField("userName", TType.STRING, (short)1);
    private static final TField PASSWORD_FIELD_DESC = new TField("password", TType.STRING, (short)2);

    public String userName;
    public String password;

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements TFieldIdEnum {
      USER_NAME((short)1, "userName"),
      PASSWORD((short)2, "password");

      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

      static {
        for (_Fields field : EnumSet.allOf(_Fields.class)) {
          byName.put(field.getFieldName(), field);
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, or null if its not found.
       */
      public static _Fields findByThriftId(int fieldId) {
        switch(fieldId) {
          case 1: // USER_NAME
            return USER_NAME;
          case 2: // PASSWORD
            return PASSWORD;
          default:
            return null;
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, throwing an exception
       * if it is not found.
       */
      public static _Fields findByThriftIdOrThrow(int fieldId) {
        _Fields fields = findByThriftId(fieldId);
        if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
        return fields;
      }

      /**
       * Find the _Fields constant that matches name, or null if its not found.
       */
      public static _Fields findByName(String name) {
        return byName.get(name);
      }

      private final short _thriftId;
      private final String _fieldName;

      _Fields(short thriftId, String fieldName) {
        _thriftId = thriftId;
        _fieldName = fieldName;
      }

      public short getThriftFieldId() {
        return _thriftId;
      }

      public String getFieldName() {
        return _fieldName;
      }
    }

    // isset id assignments

    public static final Map<_Fields, FieldMetaData> metaDataMap;
    static {
      Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.USER_NAME, new FieldMetaData("userName", TFieldRequirementType.REQUIRED,
          new FieldValueMetaData(TType.STRING)));
      tmpMap.put(_Fields.PASSWORD, new FieldMetaData("password", TFieldRequirementType.REQUIRED,
          new FieldValueMetaData(TType.STRING)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      FieldMetaData.addStructMetaDataMap(authenticate_args.class, metaDataMap);
    }

    public authenticate_args() {
    }

    public authenticate_args(
      String userName,
      String password)
    {
      this();
      this.userName = userName;
      this.password = password;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public authenticate_args(authenticate_args other) {
      if (other.isSetUserName()) {
        this.userName = other.userName;
      }
      if (other.isSetPassword()) {
        this.password = other.password;
      }
    }

    public authenticate_args deepCopy() {
      return new authenticate_args(this);
    }

    @Override
    public void clear() {
      this.userName = null;
      this.password = null;
    }

    public String getUserName() {
      return this.userName;
    }

    public authenticate_args setUserName(String userName) {
      this.userName = userName;
      return this;
    }

    public void unsetUserName() {
      this.userName = null;
    }

    /** Returns true if field userName is set (has been asigned a value) and false otherwise */
    public boolean isSetUserName() {
      return this.userName != null;
    }

    public void setUserNameIsSet(boolean value) {
      if (!value) {
        this.userName = null;
      }
    }

    public String getPassword() {
      return this.password;
    }

    public authenticate_args setPassword(String password) {
      this.password = password;
      return this;
    }

    public void unsetPassword() {
      this.password = null;
    }

    /** Returns true if field password is set (has been asigned a value) and false otherwise */
    public boolean isSetPassword() {
      return this.password != null;
    }

    public void setPasswordIsSet(boolean value) {
      if (!value) {
        this.password = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case USER_NAME:
        if (value == null) {
          unsetUserName();
        } else {
          setUserName((String)value);
        }
        break;

      case PASSWORD:
        if (value == null) {
          unsetPassword();
        } else {
          setPassword((String)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case USER_NAME:
        return getUserName();

      case PASSWORD:
        return getPassword();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case USER_NAME:
        return isSetUserName();
      case PASSWORD:
        return isSetPassword();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof authenticate_args)
        return this.equals((authenticate_args)that);
      return false;
    }

    public boolean equals(authenticate_args that) {
      if (that == null)
        return false;

      boolean this_present_userName = true && this.isSetUserName();
      boolean that_present_userName = true && that.isSetUserName();
      if (this_present_userName || that_present_userName) {
        if (!(this_present_userName && that_present_userName))
          return false;
        if (!this.userName.equals(that.userName))
          return false;
      }

      boolean this_present_password = true && this.isSetPassword();
      boolean that_present_password = true && that.isSetPassword();
      if (this_present_password || that_present_password) {
        if (!(this_present_password && that_present_password))
          return false;
        if (!this.password.equals(that.password))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public int compareTo(authenticate_args other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;
      authenticate_args typedOther = (authenticate_args)other;

      lastComparison = Boolean.valueOf(isSetUserName()).compareTo(typedOther.isSetUserName());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetUserName()) {
        lastComparison = TBaseHelper.compareTo(this.userName, typedOther.userName);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      lastComparison = Boolean.valueOf(isSetPassword()).compareTo(typedOther.isSetPassword());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetPassword()) {
        lastComparison = TBaseHelper.compareTo(this.password, typedOther.password);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      return 0;
    }

    public _Fields fieldForId(int fieldId) {
      return _Fields.findByThriftId(fieldId);
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) {
          break;
        }
        switch (field.id) {
          case 1: // USER_NAME
            if (field.type == TType.STRING) {
              this.userName = iprot.readString();
            } else {
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case 2: // PASSWORD
            if (field.type == TType.STRING) {
              this.password = iprot.readString();
            } else {
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.userName != null) {
        oprot.writeFieldBegin(USER_NAME_FIELD_DESC);
        oprot.writeString(this.userName);
        oprot.writeFieldEnd();
      }
      if (this.password != null) {
        oprot.writeFieldBegin(PASSWORD_FIELD_DESC);
        oprot.writeString(this.password);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("authenticate_args(");
      boolean first = true;

      sb.append("userName:");
      if (this.userName == null) {
        sb.append("null");
      } else {
        sb.append(this.userName);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("password:");
      if (this.password == null) {
        sb.append("null");
      } else {
        sb.append(this.password);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      if (userName == null) {
        throw new TProtocolException("Required field 'userName' was not present! Struct: " + toString());
      }
      if (password == null) {
        throw new TProtocolException("Required field 'password' was not present! Struct: " + toString());
      }
    }

  }

  public static class authenticate_result implements TBase<authenticate_result, authenticate_result._Fields>, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("authenticate_result");

    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.STRING, (short)0);
    private static final TField AE_FIELD_DESC = new TField("ae", TType.STRUCT, (short)1);

    public String success;
    public AuthenticationException ae;

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements TFieldIdEnum {
      SUCCESS((short)0, "success"),
      AE((short)1, "ae");

      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

      static {
        for (_Fields field : EnumSet.allOf(_Fields.class)) {
          byName.put(field.getFieldName(), field);
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, or null if its not found.
       */
      public static _Fields findByThriftId(int fieldId) {
        switch(fieldId) {
          case 0: // SUCCESS
            return SUCCESS;
          case 1: // AE
            return AE;
          default:
            return null;
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, throwing an exception
       * if it is not found.
       */
      public static _Fields findByThriftIdOrThrow(int fieldId) {
        _Fields fields = findByThriftId(fieldId);
        if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
        return fields;
      }

      /**
       * Find the _Fields constant that matches name, or null if its not found.
       */
      public static _Fields findByName(String name) {
        return byName.get(name);
      }

      private final short _thriftId;
      private final String _fieldName;

      _Fields(short thriftId, String fieldName) {
        _thriftId = thriftId;
        _fieldName = fieldName;
      }

      public short getThriftFieldId() {
        return _thriftId;
      }

      public String getFieldName() {
        return _fieldName;
      }
    }

    // isset id assignments

    public static final Map<_Fields, FieldMetaData> metaDataMap;
    static {
      Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT,
          new FieldValueMetaData(TType.STRING)));
      tmpMap.put(_Fields.AE, new FieldMetaData("ae", TFieldRequirementType.DEFAULT,
          new FieldValueMetaData(TType.STRUCT)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      FieldMetaData.addStructMetaDataMap(authenticate_result.class, metaDataMap);
    }

    public authenticate_result() {
    }

    public authenticate_result(
      String success,
      AuthenticationException ae)
    {
      this();
      this.success = success;
      this.ae = ae;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public authenticate_result(authenticate_result other) {
      if (other.isSetSuccess()) {
        this.success = other.success;
      }
      if (other.isSetAe()) {
        this.ae = new AuthenticationException(other.ae);
      }
    }

    public authenticate_result deepCopy() {
      return new authenticate_result(this);
    }

    @Override
    public void clear() {
      this.success = null;
      this.ae = null;
    }

    public String getSuccess() {
      return this.success;
    }

    public authenticate_result setSuccess(String success) {
      this.success = success;
      return this;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    /** Returns true if field success is set (has been asigned a value) and false otherwise */
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public AuthenticationException getAe() {
      return this.ae;
    }

    public authenticate_result setAe(AuthenticationException ae) {
      this.ae = ae;
      return this;
    }

    public void unsetAe() {
      this.ae = null;
    }

    /** Returns true if field ae is set (has been asigned a value) and false otherwise */
    public boolean isSetAe() {
      return this.ae != null;
    }

    public void setAeIsSet(boolean value) {
      if (!value) {
        this.ae = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((String)value);
        }
        break;

      case AE:
        if (value == null) {
          unsetAe();
        } else {
          setAe((AuthenticationException)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case SUCCESS:
        return getSuccess();

      case AE:
        return getAe();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case SUCCESS:
        return isSetSuccess();
      case AE:
        return isSetAe();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof authenticate_result)
        return this.equals((authenticate_result)that);
      return false;
    }

    public boolean equals(authenticate_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ae = true && this.isSetAe();
      boolean that_present_ae = true && that.isSetAe();
      if (this_present_ae || that_present_ae) {
        if (!(this_present_ae && that_present_ae))
          return false;
        if (!this.ae.equals(that.ae))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public int compareTo(authenticate_result other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;
      authenticate_result typedOther = (authenticate_result)other;

      lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(typedOther.isSetSuccess());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetSuccess()) {
        lastComparison = TBaseHelper.compareTo(this.success, typedOther.success);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      lastComparison = Boolean.valueOf(isSetAe()).compareTo(typedOther.isSetAe());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetAe()) {
        lastComparison = TBaseHelper.compareTo(this.ae, typedOther.ae);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      return 0;
    }

    public _Fields fieldForId(int fieldId) {
      return _Fields.findByThriftId(fieldId);
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) {
          break;
        }
        switch (field.id) {
          case 0: // SUCCESS
            if (field.type == TType.STRING) {
              this.success = iprot.readString();
            } else {
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case 1: // AE
            if (field.type == TType.STRUCT) {
              this.ae = new AuthenticationException();
              this.ae.read(iprot);
            } else {
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        oprot.writeString(this.success);
        oprot.writeFieldEnd();
      } else if (this.isSetAe()) {
        oprot.writeFieldBegin(AE_FIELD_DESC);
        this.ae.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("authenticate_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ae:");
      if (this.ae == null) {
        sb.append("null");
      } else {
        sb.append(this.ae);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
    }

  }

}
