# RippleHi
	The project is RippleView effect like API 5.0
	The project is support below version API 5.0
	it can be used include by eclipse and androidStudio
## Describe
	Many open resource project in github like this does not provider too much function
	Therefore, in order to facilitate the use of everyone
	This project provides 4 options for everyone to use.
## Function
* Provides a way to modify the ripple speed
* Provides a way to modify the initial transparency of ripples
* Provides settings for modifying the transparency at the end of the ripple
* Provided to modify the ripple color settings
<br/>

## Effect
![](http://i.imgur.com/d1WHdYV.png)
<br/>

## Attension
This library supports more than 4.0 API project, API no longer support below 4.0
<br/>
So you have to modify the following conditions to use the library
<br/><br/>
1. Download the latest SDK , version sdk must > 25.0.2<br/>
2. change the Module:app build.gradle like this<br/>

	defaultConfig {
	    minSdkVersion 14			// it must be >= API 14
	    targetSdkVersion 25			// this is must be 25
	 }


## How to used
![](http://i.imgur.com/Cn4qUgi.jpg)
<br/>
### Step
1. **Add it in your root build.gradle at the end of repositories**

		allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}
2.  **Add the dependency**

		dependencies {
		    compile 'com.github.tom200989:RippleHii:v1'
		}
3. **Copy the codes In your layout**

	    <com.ripplehi.ripplehi.RippleHii
            android:id = "@+id/rippler_notice_item"
            android:layout_width = "match_parent"
            android:layout_height = "match_parent"
            app:color = "@color/colorPrimary"
            app:maxAlpha = "0.31"
            app:minAlpha = "0.25"
            app:speed = "2.0" />
3.1. attension

		don't forgot your space attrs setting
		xmlns:app="http://schemas.android.com/apk/res-auto"
4. **Copy the codes In your Java class like MainActivity**

 		ripple.setOnRippleListener(new RippleHii.RippleListener() {
            @Override
            public void finish() {
			// todo what you want to do
        });

## Use with RecycleView onItemClick 
!!!! 结合recycleview使用的时候特别需要注意的问题 !!!!
### attension
	Of particular note is that the use of recycleview can lead to the problem of simultaneous triggering between two item, 
	the reason for this problem is that the user in a short period of time to touch the two consecutive item
### Effect
![](http://i.imgur.com/sDEKOW8.png)
### how to resolve
like this
<br/>

	1.Provide a temporary boolean variable<br/>
		boolean flag = true;

	2.Determine whether the real variable and the temporary variable are equal
		holder.ripple.setOnRippleListener(new HiRipple.RippleListener() {
	
	            @Override
	            public void finish(boolean locked) {

				/*
				 * In order to prevent users from multiple Item multiple clicks
				 * Locked by external flag
				 */

				// ******************* important code, Copy It!!!! ******************* //

			       if(flag){
					   
					// 1.locked the flag
					flag = false;

					// 2.do you business
					// ......

					// 3.when you finish, unlocked the flag
					flag = true;

				   }

				// ******************* important code, Copy It!!!! ******************* //

	            }
	
	        });
For Chinese
<br/>
	
	与recycleview结合使用时候特别需要注意的问题
	为了防止用户对列表的多个item进行频繁的点击而导致数据加载错乱, 建议采用如上的方法
	1. 设置一个外部的flag , 默认为true
	2. 当第一次点击时, 波纹开始播放, 播放完毕, 开始执行操作数据的逻辑,  此时设置flag = false;进行锁定
	3. 当执行完你的数据逻辑业务逻辑后, 恢复flag = true.允许下次可点
	
	这样做的好处是, 尽管用户在频繁点击多个item的时候, 依然能看到波纹播放的动画, 但是由于数据的操作进行了加锁操作
	用户只看到了item被点击, 但对应item的业务逻辑由于上一次的点击flag没有恢复为true,而无法继续操作
	只有等上一次的业务逻辑完全执行完毕后, flag才被允许修改回true, 此时方可继续执行下一次点击的逻辑

<br/>
![](http://i.imgur.com/aIw4vT2.jpg)
<br/>

### step
1. **download the project<br/>**
	click the button of <font size = "4" color ="#0099ff">Download ZIP</font><br/>
![](http://i.imgur.com/Iu9I200.png)
<br/><br/>
2. **Copy the File of following path to your project java directory**<br/>
![](http://i.imgur.com/YhdVv7c.png)
<br/><br/>
3. **Copy the `attrs.xml` to your values directory**<br/>
![](http://i.imgur.com/b9t75jl.png)
<br/><br/>
4. **Copy the codes In your layout**

	    <com.ripplehi.ripplehi.RippleHii
            android:id = "@+id/rippler_notice_item"
            android:layout_width = "match_parent"
            android:layout_height = "match_parent"
            app:color = "@color/colorPrimary"
            app:maxAlpha = "0.31"
            app:minAlpha = "0.25"
            app:speed = "2.0" />
4.1. attension

		don't forgot your space attrs setting
		xmlns:app="http://schemas.android.com/apk/res-auto"
5. **Copy the codes In your Java class like MainActivity**

 		ripple.setOnRippleListener(new RippleHii.RippleListener() {
            @Override
            public void finish() {
			// todo what you want to do
        });
## Attrs Describe
<table border="1">
<th align="center">Name</th>
<th align="center">Describe</th>
<th align="center">range</th>
<th align="center">default</th>

<tr>
<td align="left">speed</td>
<td align="center">This property is used to set the speed of the ripple release</td>
<td align="center">0.0 ~ 2.0</td>
<td align="center">1.2</td>
</tr>

<tr>
<td align="left">maxAlpha</td>
<td align="center">This property is used to set the transparency of the beginning of the ripple</td>
<td align="center">0.0 ~ 1.0</td>
<td align="center">0.31</td>
</tr>

<tr>
<td align="left">minAlpha</td>
<td align="center">This property is used to set the transparency at the end of the ripples</td>
<td align="center">0.0 ~ 1.0</td>
<td align="center">0.25</td>
</tr>

<tr>
<td align="left">color</td>
<td align="center">This property is used to set the color of the ripples</td>
<td align="center">@color/colorAccent</td>
<td align="center">@color/colorAccent</td>
</tr>

</table>

## CallBack
	If you want the ripple effect to disappear and execute your business logic,
	call the ripple control callback method within the class that calls the ripple control
like this sample
<br/>

 		ripple.setOnRippleListener(new RippleHii.RippleListener() {
            @Override
            public void finish() {
			// todo what you want to do
        });

