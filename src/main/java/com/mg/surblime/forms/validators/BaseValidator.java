package com.mg.surblime.forms.validators;

import android.util.Pair;

import com.mg.surblime.forms.fields.FormField;

import java.util.ArrayList;

/**
 * Created by moses on 2/22/19.
 */

public class BaseValidator<T extends FormField<S, ?>, S, Q extends BaseValidator<?, ?, ?>> {

    protected final Q self;
    protected T t;
    private ArrayList<Pair<String, Evaluator<S>>> evaluators = new ArrayList<>();
    private Evaluator<S> notEmptyEvaluator = getNotEmptyEvaluator();

    public BaseValidator(final Class<Q> selfClass) {
        this.self = selfClass.cast(this);
    }

    public T get() {
        return t;
    }

    public Evaluator<S> getNotEmptyEvaluator() {
        return value -> value == null;
    }

    public Q begin(T t) {
        this.t = t;
        evaluators.clear();
        if (t != null) {
            t.setError(null);
        }
        return self;
    }

    public Q custom(String error, Evaluator<S> evaluator) {
        evaluators.add(new Pair<>(error, evaluator));
        return self;
    }

    public S getValue() {
        if (t == null) {
            return null;
        }
        return t.get();
    }

    public Q notEmpty() {
        return notEmpty("This field is required!");
    }

    public Q notEmpty(String message) {
        evaluators.add(new Pair<>(message, this.notEmptyEvaluator));
        return self;
    }

    public Q equals(S s, String message) {
        evaluators.add(new Pair<>(message, (value -> !value.equals(s))));
        return self;
    }

    public void end() {
        isValid(this.t, true);
    }

    public boolean isValid(T t) {
        return isValid(t, false);
    }

    public boolean isValid(T t, boolean setError) {
        this.t = t;
        if (t == null) {
            return true;
        }
        if (!evaluators.isEmpty()) {
            for (Pair<String, Evaluator<S>> e : evaluators) {
                if (e.second == notEmptyEvaluator || getValue() != null) {
                    if (e.second.isInvalid(getValue())) {
                        if (setError) {
                            t.setError(e.first);
                        }
                        return false;
                    }
                }
            }
        }
        t.setError(null);
        return true;
    }

    public void run(T t) {
        this.t = t;
        run();
    }

    public void run() {
        end();
    }

    public interface Evaluator<S> {
        /**
         * @param value
         * @return
         */
        boolean isInvalid(S value);
    }
}
