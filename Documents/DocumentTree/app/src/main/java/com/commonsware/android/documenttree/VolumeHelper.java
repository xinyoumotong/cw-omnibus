/***
 Copyright (c) 2016 CommonsWare, LLC
 Licensed under the Apache License, Version 2.0 (the "License"); you may not
 use this file except in compliance with the License. You may obtain a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 OF ANY KIND, either express or implied. See the License for the specific
 language governing permissions and limitations under the License.

 From _The Busy Coder's Guide to Android Development_
 https://commonsware.com/Android
 */

package com.commonsware.android.documenttree;

import android.content.Intent;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.preference.ListPreference;
import android.preference.Preference;
import java.util.List;

public class VolumeHelper extends TreeUriPreferenceHelper
  implements Preference.OnPreferenceChangeListener {
  private final String uriKey;
  private final String dirName;

  public VolumeHelper(Host host, ListPreference pref, String uriKey,
                      String dirName) {
    super(host, pref);

    this.uriKey=uriKey;
    this.dirName=dirName;
    pref.setOnPreferenceChangeListener(this);
  }

  @Override
  protected String getUriKey() {
    return(uriKey);
  }

  @Override
  public boolean onPreferenceChange(Preference pref,
                                    Object o) {
    StorageManager storage=
      pref.getContext().getSystemService(StorageManager.class);
    List<StorageVolume> volumes=storage.getStorageVolumes();
    String uuid=o.toString();

    for (StorageVolume volume : volumes) {
      if ((volume.getUuid()==null &&
        uuid.equals(SettingsFragment.STORAGE_FAKE_UUID)) ||
        (uuid.equals(volume.getUuid()))) {
        Intent i=volume.createAccessIntent(dirName);

        host.startActivityForHelper(i, this);
        break;
      }
    }

    return(true);
  }
}
