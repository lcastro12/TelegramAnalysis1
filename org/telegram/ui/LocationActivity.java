package org.telegram.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.TL_messageForwarded;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.objects.MessageObject;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;

public class LocationActivity extends BaseFragment implements NotificationCenterDelegate {
    private BackupImageView avatarImageView;
    private View bottomView;
    private TextView distanceTextView;
    private boolean firstWas = false;
    private GoogleMap googleMap;
    public SupportMapFragment mapFragment = new C09501();
    private MessageObject messageObject;
    private Location myLocation;
    private TextView nameTextView;
    private TextView sendButton;
    private Location userLocation;
    private boolean userLocationMoved = false;
    private Marker userMarker;

    class C09501 extends SupportMapFragment {

        class C05232 implements OnClickListener {
            C05232() {
            }

            public void onClick(View view) {
                NotificationCenter.Instance.postNotificationName(997, Double.valueOf(LocationActivity.this.userLocation.getLatitude()), Double.valueOf(LocationActivity.this.userLocation.getLongitude()));
                LocationActivity.this.finishFragment();
            }
        }

        class C05244 implements OnClickListener {
            C05244() {
            }

            public void onClick(View view) {
                if (LocationActivity.this.userLocation != null) {
                    LocationActivity.this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocationActivity.this.userLocation.getLatitude(), LocationActivity.this.userLocation.getLongitude()), LocationActivity.this.googleMap.getMaxZoomLevel() - 8.0f));
                }
            }
        }

        class C08671 implements OnMyLocationChangeListener {
            C08671() {
            }

            public void onMyLocationChange(Location location) {
                LocationActivity.this.positionMarker(location);
            }
        }

        class C08683 implements OnMarkerDragListener {
            C08683() {
            }

            public void onMarkerDragStart(Marker marker) {
            }

            public void onMarkerDrag(Marker marker) {
                LocationActivity.this.userLocationMoved = true;
            }

            public void onMarkerDragEnd(Marker marker) {
                LatLng latLng = marker.getPosition();
                LocationActivity.this.userLocation.setLatitude(latLng.latitude);
                LocationActivity.this.userLocation.setLongitude(latLng.longitude);
            }
        }

        C09501() {
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            LocationActivity.this.googleMap = getMap();
            if (LocationActivity.this.googleMap != null) {
                LocationActivity.this.googleMap.setMyLocationEnabled(true);
                LocationActivity.this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                LocationActivity.this.googleMap.getUiSettings().setZoomControlsEnabled(false);
                LocationActivity.this.googleMap.getUiSettings().setCompassEnabled(false);
                LocationActivity.this.googleMap.setOnMyLocationChangeListener(new C08671());
                LocationActivity.this.myLocation = LocationActivity.this.googleMap.getMyLocation();
                if (LocationActivity.this.sendButton != null) {
                    LocationActivity.this.userLocation = new Location("network");
                    LocationActivity.this.userLocation.setLatitude(20.659322d);
                    LocationActivity.this.userLocation.setLongitude(-11.40625d);
                    LocationActivity.this.userMarker = LocationActivity.this.googleMap.addMarker(new MarkerOptions().position(new LatLng(20.659322d, -11.40625d)).icon(BitmapDescriptorFactory.fromResource(C0419R.drawable.map_pin)).draggable(true));
                    LocationActivity.this.sendButton.setOnClickListener(new C05232());
                    LocationActivity.this.googleMap.setOnMarkerDragListener(new C08683());
                }
                if (LocationActivity.this.bottomView != null) {
                    LocationActivity.this.bottomView.setOnClickListener(new C05244());
                }
                if (LocationActivity.this.messageObject != null) {
                    int fromId = LocationActivity.this.messageObject.messageOwner.from_id;
                    if (LocationActivity.this.messageObject.messageOwner instanceof TL_messageForwarded) {
                        fromId = LocationActivity.this.messageObject.messageOwner.fwd_from_id;
                    }
                    User user = (User) MessagesController.Instance.users.get(Integer.valueOf(fromId));
                    if (user != null) {
                        LocationActivity.this.avatarImageView.setImage(user.photo.photo_small, "50_50", Utilities.getUserAvatarForId(user.id));
                        LocationActivity.this.nameTextView.setText(Utilities.formatName(user.first_name, user.last_name));
                    }
                    LocationActivity.this.userLocation = new Location("network");
                    LocationActivity.this.userLocation.setLatitude(LocationActivity.this.messageObject.messageOwner.media.geo.lat);
                    LocationActivity.this.userLocation.setLongitude(LocationActivity.this.messageObject.messageOwner.media.geo._long);
                    LatLng latLng = new LatLng(LocationActivity.this.userLocation.getLatitude(), LocationActivity.this.userLocation.getLongitude());
                    LocationActivity.this.userMarker = LocationActivity.this.googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(C0419R.drawable.map_pin)));
                    LocationActivity.this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, LocationActivity.this.googleMap.getMaxZoomLevel() - 8.0f));
                }
                LocationActivity.this.positionMarker(LocationActivity.this.myLocation);
                ViewGroup topLayout = (ViewGroup) LocationActivity.this.parentActivity.findViewById(C0419R.id.container);
                topLayout.requestTransparentRegion(topLayout);
            }
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.messageObject = (MessageObject) NotificationCenter.Instance.getFromMemCache(0);
        NotificationCenter.Instance.addObserver(this, 5);
        if (this.messageObject != null) {
            NotificationCenter.Instance.addObserver(this, 3);
        }
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 3);
        NotificationCenter.Instance.removeObserver(this, 5);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void applySelfActionBar() {
        if (this.parentActivity != null) {
            ActionBar actionBar = this.parentActivity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setSubtitle(null);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setCustomView(null);
            if (this.messageObject != null) {
                actionBar.setTitle(getStringEntry(C0419R.string.ChatLocation));
            } else {
                actionBar.setTitle(getStringEntry(C0419R.string.ShareLocation));
            }
            TextView title = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_title);
            if (title == null) {
                title = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
            }
            if (title != null) {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                title.setCompoundDrawablePadding(0);
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((ApplicationActivity) this.parentActivity).showActionBar();
            ((ApplicationActivity) this.parentActivity).updateActionBar();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            if (this.messageObject != null) {
                this.fragmentView = inflater.inflate(C0419R.layout.location_view_layout, container, false);
            } else {
                this.fragmentView = inflater.inflate(C0419R.layout.location_attach_layout, container, false);
            }
            this.avatarImageView = (BackupImageView) this.fragmentView.findViewById(C0419R.id.location_avatar_view);
            this.nameTextView = (TextView) this.fragmentView.findViewById(C0419R.id.location_name_label);
            this.distanceTextView = (TextView) this.fragmentView.findViewById(C0419R.id.location_distance_label);
            this.bottomView = this.fragmentView.findViewById(C0419R.id.location_bottom_view);
            this.sendButton = (TextView) this.fragmentView.findViewById(C0419R.id.location_send_button);
            getChildFragmentManager().beginTransaction().replace(C0419R.id.map_view, this.mapFragment).commit();
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    private void updateUserData() {
        if (this.messageObject != null && this.avatarImageView != null) {
            int fromId = this.messageObject.messageOwner.from_id;
            if (this.messageObject.messageOwner instanceof TL_messageForwarded) {
                fromId = this.messageObject.messageOwner.fwd_from_id;
            }
            User user = (User) MessagesController.Instance.users.get(Integer.valueOf(fromId));
            if (user != null) {
                FileLocation photo = null;
                if (user.photo != null) {
                    photo = user.photo.photo_small;
                }
                this.avatarImageView.setImage(photo, null, Utilities.getUserAvatarForId(user.id));
                this.nameTextView.setText(Utilities.formatName(user.first_name, user.last_name));
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0419R.menu.location_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finishFragment();
                break;
            case C0419R.id.map_to_my_location:
                if (this.myLocation != null) {
                    LatLng latLng = new LatLng(this.myLocation.getLatitude(), this.myLocation.getLongitude());
                    if (this.googleMap != null) {
                        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, this.googleMap.getMaxZoomLevel() - 8.0f));
                        break;
                    }
                }
                break;
            case C0419R.id.map_list_menu_map:
                if (this.googleMap != null) {
                    this.googleMap.setMapType(1);
                    break;
                }
                break;
            case C0419R.id.map_list_menu_satellite:
                if (this.googleMap != null) {
                    this.googleMap.setMapType(2);
                    break;
                }
                break;
            case C0419R.id.map_list_menu_hybrid:
                if (this.googleMap != null) {
                    this.googleMap.setMapType(4);
                    break;
                }
                break;
        }
        return true;
    }

    private void positionMarker(Location location) {
        if (location != null) {
            this.myLocation = location;
            if (this.messageObject != null) {
                if (this.userLocation != null && this.distanceTextView != null) {
                    if (location.distanceTo(this.userLocation) < 1000.0f) {
                        this.distanceTextView.setText(String.format("%d %s", new Object[]{Integer.valueOf((int) distance), ApplicationLoader.applicationContext.getString(C0419R.string.MetersAway)}));
                        return;
                    }
                    this.distanceTextView.setText(String.format("%.2f %s", new Object[]{Float.valueOf(distance / 1000.0f), ApplicationLoader.applicationContext.getString(C0419R.string.KMetersAway)}));
                }
            } else if (!this.userLocationMoved && this.googleMap != null) {
                this.userLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                this.userMarker.setPosition(latLng);
                if (this.firstWas) {
                    this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    return;
                }
                this.firstWas = true;
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, this.googleMap.getMaxZoomLevel() - 8.0f));
            }
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == 3) {
            int mask = ((Integer) args[0]).intValue();
            if ((mask & 2) != 0 || (mask & 1) != 0) {
                updateUserData();
            }
        } else if (id == 5) {
            removeSelfFromStack();
        }
    }
}
