package com.koyomiji.asmine.query;

public class AbstractQuery<T> {
  protected T parent;
    protected QueryContext context;

    public AbstractQuery(T parent) {
        this.parent = parent;

        if (parent instanceof AbstractQuery<?>) {
            this.context = ((AbstractQuery<?>) parent).context;
        } else {
            this.context = new QueryContext();
        }
    }

    public QueryContext getContext() {
        return context;
    }

    public T done() {
        return parent;
    }
}
