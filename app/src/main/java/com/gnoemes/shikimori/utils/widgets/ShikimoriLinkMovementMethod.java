package com.gnoemes.shikimori.utils.widgets;


import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.gnoemes.shikimori.entity.common.domain.Type;
import com.gnoemes.shikimori.presentation.view.common.widget.shikimori.ShikimoriTextView;

import java.util.Arrays;
import java.util.List;

public class ShikimoriLinkMovementMethod extends LinkMovementMethod {

    private static ShikimoriTextView.LinkClickListener clickListener;

    private static ShikimoriLinkMovementMethod linkMovementMethod = new ShikimoriLinkMovementMethod();

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] links = buffer.getSpans(off, off, ClickableSpan.class);

            if (links.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    String url = ((URLSpan) links[0]).getURL();
                    if (URLUtil.isNetworkUrl(url)) {
                        links[0].onClick(widget);
                    } else {
                        List<String> list = Arrays.asList(url.split("_"));
                        clickListener.onLinkClicked(Type.valueOf(list.get(0)), Long.parseLong(list.get(1)));
                    }
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(links[0]),
                            buffer.getSpanEnd(links[0]));
                }
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    public static android.text.method.MovementMethod getInstance(ShikimoriTextView.LinkClickListener listener) {
        clickListener = listener;
        return linkMovementMethod;
    }
}
