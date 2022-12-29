/*
 * Copyright (C) 2015 Tomás Ruiz-López.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zqw.mobile.grainfull.mvp.ui.widget.secondarylist;


import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * 页眉
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {

    protected TextView titleText = null;

    public HeaderViewHolder(View itemView, @IdRes int titleID) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(titleID);
    }

    public void render(String title){
        titleText.setText(title);
    }

}
