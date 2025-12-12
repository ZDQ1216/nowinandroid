/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.core.model.data

/**
 * 用户偏好设置数据类
 *
 * @property bookmarkedNewsResources 已收藏的新闻资源ID集合
 * @property viewedNewsResources 已浏览的新闻资源ID集合
 * @property followedTopics 关注的话题ID集合
 * @property themeBrand 应用主题品牌配置
 * @property darkThemeConfig 深色主题配置选项
 * @property useDynamicColor 是否使用动态颜色主题
 * @property shouldHideOnboarding 是否隐藏新手引导界面
 */
data class UserData(
    val bookmarkedNewsResources: Set<String>,
    val viewedNewsResources: Set<String>,
    val followedTopics: Set<String>,
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
)
