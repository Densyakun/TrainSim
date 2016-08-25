package net.densyakun.trainsim.android;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import net.densyakun.trainsim.RailwayManager;
import net.densyakun.trainsim.pack.jreast.RailwayPack_KeihinTohokuLine;
public class MainActivity extends Activity {
    private RailwayManager railwaymanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        (railwaymanager = new RailwayManager()).setUpdateinterval(1000);
        HorizontalScrollView hsv = new HorizontalScrollView(this);
        ScrollView sv = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.addView(new TIDView(this));
        sv.addView(layout);
        hsv.addView(sv);
        setContentView(hsv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        railwaymanager.setFast(false);
        railwaymanager.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        railwaymanager.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu ti = menu.addSubMenu("時間...");
        ti.add("再生");
        ti.add("更新");
        ti.add("倍速切り替え");
        ti.add("停止");
        menu.add("[test]");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("再生")) {
            railwaymanager.play();
        } else if (item.getTitle().equals("更新")) {
            railwaymanager.update();
        } else if (item.getTitle().equals("倍速切り替え")) {
            railwaymanager.setFast(!railwaymanager.isFast());
        } else if (item.getTitle().equals("停止")) {
            railwaymanager.stop();
        } else if (item.getTitle().equals("[test]")) {
            railwaymanager.loadRailwayPack(new RailwayPack_KeihinTohokuLine());
        }
        return true;
    }
    public RailwayManager getRailwayManager() {
        return railwaymanager;
    }
}
