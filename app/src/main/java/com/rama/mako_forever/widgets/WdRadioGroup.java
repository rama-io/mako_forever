package com.rama.mako_forever.widgets;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class WdRadioGroup extends LinearLayout {

    private int checkedId = -1;

    private boolean protectFromCheckedChange;

    private OnCheckedChangeListener listener;

    public WdRadioGroup(Context context) {
        super(context);
        init();
    }

    public WdRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    // ------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------

    public int getCheckedRadioButtonId() {
        return checkedId;
    }

    public void check(int id) {

        if (id == -1) {
            clearCheck();
            return;
        }

        View view = findViewById(id);

        if (!(view instanceof WdRadio)) {
            return;
        }

        WdRadio radio = (WdRadio) view;

        if (checkedId == id && radio.isChecked()) {
            return;
        }

        protectFromCheckedChange = true;

        if (checkedId != -1) {
            View oldView = findViewById(checkedId);

            if (oldView instanceof WdRadio) {
                ((WdRadio) oldView).setChecked(false);
            }
        }

        radio.setChecked(true);

        protectFromCheckedChange = false;

        setCheckedId(id);
    }

    public void clearCheck() {

        if (checkedId == -1) {
            return;
        }

        protectFromCheckedChange = true;

        View view = findViewById(checkedId);

        if (view instanceof WdRadio) {
            ((WdRadio) view).setChecked(false);
        }

        protectFromCheckedChange = false;

        setCheckedId(-1);
    }

    public void setOnCheckedChangeListener(
            OnCheckedChangeListener listener) {

        this.listener = listener;
    }

    // ------------------------------------------------------------
    // Child management
    // ------------------------------------------------------------

    @Override
    public void addView(
            View child,
            int index,
            ViewGroup.LayoutParams params) {

        super.addView(child, index, params);

        if (child instanceof WdRadio) {
            WdRadio radio = (WdRadio) child;

            radio.setInternalCheckedChangeListener(
                    new WdRadio.InternalOnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(
                                WdRadio radio,
                                boolean checked) {

                            if (protectFromCheckedChange) {
                                return;
                            }

                            if (checked) {
                                check(radio.getId());
                            } else if (checkedId == radio.getId()) {
                                /*
                                 * A radio cannot normally be unchecked
                                 * by the user. This handles programmatic
                                 * changes safely.
                                 */
                                protectFromCheckedChange = true;
                                radio.setChecked(true);
                                protectFromCheckedChange = false;
                            }
                        }
                    }
            );

            if (radio.isChecked()) {
                check(radio.getId());
            }
        }
    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();

        /*
         * Find the initially checked radio after all XML
         * children have been inflated.
         */
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);

            if (child instanceof WdRadio) {

                WdRadio radio = (WdRadio) child;

                if (radio.isChecked()) {
                    check(radio.getId());
                    break;
                }
            }
        }
    }

    private void setCheckedId(int id) {

        if (checkedId == id) {
            return;
        }

        checkedId = id;

        if (listener != null) {
            listener.onCheckedChanged(
                    this,
                    checkedId
            );
        }
    }

    // ------------------------------------------------------------
    // State saving
    // ------------------------------------------------------------

    @Override
    protected Parcelable onSaveInstanceState() {

        Parcelable superState = super.onSaveInstanceState();

        SavedState state = new SavedState(superState);
        state.checkedId = checkedId;

        return state;
    }

    @Override
    protected void onRestoreInstanceState(
            Parcelable state) {

        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;

        super.onRestoreInstanceState(
                savedState.getSuperState()
        );

        if (savedState.checkedId != -1) {
            check(savedState.checkedId);
        } else {
            clearCheck();
        }
    }

    private static class SavedState
            extends BaseSavedState {

        int checkedId;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);

            checkedId = in.readInt();
        }

        @Override
        public void writeToParcel(
                Parcel out,
                int flags) {

            super.writeToParcel(out, flags);

            out.writeInt(checkedId);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {

                    @Override
                    public SavedState createFromParcel(
                            Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    // ------------------------------------------------------------
    // Listener
    // ------------------------------------------------------------

    public interface OnCheckedChangeListener {

        void onCheckedChanged(
                WdRadioGroup group,
                int checkedId
        );
    }
}