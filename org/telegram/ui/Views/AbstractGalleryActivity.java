package org.telegram.ui.Views;

import org.telegram.objects.MessageObject;

public abstract class AbstractGalleryActivity extends PausableActivity {
    public abstract void didShowMessageObject(MessageObject messageObject);

    public abstract void topBtn();
}
