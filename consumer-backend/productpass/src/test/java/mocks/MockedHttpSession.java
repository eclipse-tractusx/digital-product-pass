package mocks;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import utils.DateTimeUtil;

import java.util.*;
import java.util.stream.Collectors;

public class MockedHttpSession implements HttpSession {

    private Map<String, Object> attributes;
    private String id;
    private long creationTime;

    public MockedHttpSession() {
        attributes = new HashMap<String, Object>();
        id = UUID.randomUUID().toString();
        creationTime = DateTimeUtil.getTimestamp();
    }
    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int i) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public Object getAttribute(String s) {
        return attributes.get(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return (Enumeration<String>) attributes.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public void setAttribute(String s, Object o) {
        attributes.put(s, o);
    }

    @Override
    public void removeAttribute(String s) {
        attributes.remove(s);
    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return false;
    }
}
