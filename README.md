# WIP
This is a work in progress repository for [Appnouncements](https://www.appnouncements.com), a startup focused on integrating rich release notes into your native apps as easily as possible!

**This is NOT ready to be integrated into any projects at this stage.**

# Appnouncements Android SDK
The Appnouncements Android SDK is the best way to get your release notes in front of your customer's eyes. Use it to inform customers about relevant changes and updates to your app.

## Requirements

- **minSDK:** 15
- **SDK Size:** 64 methods
- **Dependencies:** `appcompat-v7:22.1.0 or higher`
- **Permissions:** `android.permission.INTERNET`

## Integration Guide
In your project's root `build.gradle`, add jitpack as a repository:

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the dependency to your project

```
	dependencies {
	  implementation 'com.github.Appnouncements:appnouncements-android:881ff1e18f'
	}
```

**Note: If you already include appcompat-v7, you will likely run into version incompatabilities. To resolve these, see our troubleshooting section.**

In your [Application Class](https://github.com/codepath/android_guides/wiki/Understanding-the-Android-Application-Class#defining-your-application-class), initialize Appnouncements with the SDK key that can be found in your dashboard.

```
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Appnouncements.initialize(this, "YOUR API KEY HERE");
    }
}
```

Retrieve an instance of the Appnouncements' `Client` by calling `Appnouncements.getClientAsync` and implementing our `Client.Listener` interface.
```
public class MainActivity extends AppCompatActivity implements Client.Listener {
    @Override
    protected void onResume() {
        super.onResume();
        Appnouncements.getClientAsync(this);
    }

    @Override
    public void onAppnouncementsClientReady(Client client) {
        client.showReleaseNotes(this);
    }
}
```

## Troubleshooting
> Android dependency 'com.android.support:appcompat-v7' has different version for the compile (...) and runtime (...) classpath. You should manually set the same version via DependencyResolution

Fix 1 **(Recommended)**: Force appcompat-v7 dependency resolution to the one specified in your build.gradle which causes all your dependencies to rely on the same appcompat version.
```
  dependencies {
    implementation ('com.android.support:appcompat-v7:22.1.0') { force = true }
    implementation 'com.github.Appnouncements:appnouncements-android:881ff1e18f'
  }
```

Fix 2: Exclude the appcompat modules from Appnouncements
```
  dependencies {
    implementation 'com.android.support:appcompat-v7:22.1.0'
    implementation ('com.github.Appnouncements:appnouncements-android:881ff1e18f') { exclude group: 'com.android.support' }
  }
```

## TODO:
- Theming/Custom Strings
- API docs