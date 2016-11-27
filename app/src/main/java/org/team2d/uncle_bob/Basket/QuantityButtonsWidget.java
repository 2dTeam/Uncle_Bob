package org.team2d.uncle_bob.Basket;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;
import org.team2d.uncle_bob.Database.ORM.Items.ItemParams;
import org.team2d.uncle_bob.R;

public class QuantityButtonsWidget {
    private final LinearLayout container;
    private final View.OnClickListener onClick;
    private final OnStateChangedListener onRefresh;
    private BasketItem item;
    private View buttonBuy;
    private View buttonIncrease;
    private View buttonDecrease;
    private EditText textQuantity;
    private QuantityWatcher quantityWatcher;

    public interface OnStateChangedListener {
        void act(BasketItem item);
    }

    public QuantityButtonsWidget(LayoutInflater inflater, LinearLayout container, ItemObject item, ItemParams details, @Nullable View.OnClickListener onClick, @Nullable OnStateChangedListener onRefresh) {
        this.container = container;
        this.onClick = onClick;
        this.onRefresh = onRefresh;
        setBasketItem(item, details);

        inflater.inflate(R.layout.quantity_buttons_details_fragment, container);

        buttonBuy = container.findViewById(R.id.quantity_widget_button_buy);
        buttonIncrease = container.findViewById(R.id.quantity_widget_button_increase);
        buttonDecrease = container.findViewById(R.id.quantity_widget_button_decrease);
        textQuantity = (EditText) container.findViewById(R.id.quantity_widget_text_quantity);

        buttonBuy.setOnClickListener(new QuantityChanger(+1));
        buttonIncrease.setOnClickListener(new QuantityChanger(+1));
        buttonDecrease.setOnClickListener(new QuantityChanger(-1));

        quantityWatcher = new QuantityWatcher();
        textQuantity.addTextChangedListener(quantityWatcher);
    }

    public BasketItem refresh() {
        if (item.getQuantity() == 0) {
            clearQuantityButtons();
            Basket.getInstance().removeItem(item);
        } else {
            showQuantityButtons();
            Basket.getInstance().addItem(item);
        }

        textQuantity.setText(String.valueOf(item.getQuantity()));
        if (onRefresh != null)
            onRefresh.act(item);
        return item;
    }

    public void changeItemParameters(ItemParams details) {
        setBasketItem(item.getItem(), details);
        refresh();
    }

    public void destroyListeners() {
        buttonBuy.setOnClickListener(null);
        buttonIncrease.setOnClickListener(null);
        buttonDecrease.setOnClickListener(null);
        textQuantity.setOnClickListener(null);
        textQuantity.removeTextChangedListener(quantityWatcher);
        //onClick = null;
    }

    private void setBasketItem(ItemObject itemObject, ItemParams details) {
        this.item = Basket.getInstance().getItem(itemObject, details);
        if (this.item == null)
            this.item = new BasketItem(itemObject, details);
    }

    private void clearQuantityButtons() {
        buttonBuy.setVisibility(View.VISIBLE);

        buttonIncrease.setVisibility(View.GONE);
        buttonDecrease.setVisibility(View.GONE);
        textQuantity.setVisibility(View.GONE);
    }

    private void showQuantityButtons() {
        buttonBuy.setVisibility(View.GONE);

        buttonIncrease.setVisibility(View.VISIBLE);
        buttonDecrease.setVisibility(View.VISIBLE);
        textQuantity.setVisibility(View.VISIBLE);
    }

    private class QuantityChanger implements View.OnClickListener {
        private final int delta;

        public QuantityChanger(int delta) {
            this.delta = delta;
        }

        @Override
        public void onClick(View v) {
            final int newValue = item.getQuantity() + delta;
            if (newValue < 0)
                return;

            item.setQuantity(item.getQuantity() + delta);
            refresh();
            if (onClick != null)
                onClick.onClick(v);
        }
    }

    private class QuantityWatcher implements TextWatcher {
        String before = null;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            before = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(before))
                try {
                    int newValue;
                    if (s.toString().isEmpty())
                        newValue = 0;
                    else
                        newValue = Integer.parseInt(s.toString());

                    if (newValue < 0) {
                        s.clear();
                        s.append(before);
                        return;
                    }
                    item.setQuantity(newValue);
                    refresh();
                } catch (NumberFormatException e) {
                    s.clear();
                    s.append(before);
                }
        }
    }
}
