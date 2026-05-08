# BeforeAfterImageView

A modern AI-style before/after image comparison slider for Android.

`BeforeAfterImageView` provides a smooth draggable image comparison experience similar to apps like:
- Remini
- Pixelcut
- Canva AI
- Lightroom
- PicsArt AI

Perfect for:
- AI image generation apps
- Photo restoration apps
- AI upscaling apps
- Background removal apps
- Image enhancement previews
- Before/after showcases

---

# ✨ Features

- Smooth draggable compare slider
- Modern AI-style compare handle
- Left/right arrow indicator
- Animated reveal effect
- Auto compare animation
- Haptic feedback
- Lightweight and reusable
- XML customizable
- Clean architecture
- Memory leak safe
- Android View system support

---

# 📸 Preview

```xml
<com.jldevelopers.beforeafter.BeforeAfterImageView
    android:id="@+id/beforeAfterView"
    android:layout_width="match_parent"
    android:layout_height="300dp"/>
```

---

# 📦 Installation

## 1. Add Module Dependency

```gradle
implementation project(":beforeafter")
```

---

# 🛠 Setup

## XML Usage

```xml
<com.jldevelopers.beforeafter.BeforeAfterImageView
    android:id="@+id/beforeAfterView"
    android:layout_width="match_parent"
    android:layout_height="300dp"/>
```

---

## Java Usage

```java
BeforeAfterImageView beforeAfterView =
        findViewById(R.id.beforeAfterView);

// Set original image
beforeAfterView.setOriginalBitmap(originalBitmap);

// Set generated/result image
beforeAfterView.setResultDrawable(resultDrawable);

// Play reveal animation
beforeAfterView.playRevealAnimation();

// Play automatic compare animation
beforeAfterView.playCompareAnimation();
```

---

# 🎨 Customization

## XML Attributes

```xml
app:dividerColor="@android:color/white"
app:handleSize="56dp"
app:dividerWidth="2dp"
app:autoAnimate="true"
```

---

# 🧠 How It Works

The view stacks:
1. Original image
2. Result image
3. Draggable compare slider

The result image is clipped dynamically while dragging to create the comparison effect.

---

# 📱 Requirements

| Requirement | Version |
|---|---|
| Min SDK | 24 |
| Compile SDK | 36 |
| Java | 11 |

---

# 📚 Public Methods

| Method | Description |
|---|---|
| `setOriginalBitmap()` | Sets original image |
| `setResultDrawable()` | Sets generated image |
| `clear()` | Clears all images |
| `playRevealAnimation()` | Plays intro animation |
| `playCompareAnimation()` | Plays automatic compare animation |

---

# 🧩 Example

## ConvertActivity.java

```java
Glide.with(this)
        .load(imageUrl)
        .into(new CustomTarget<Drawable>() {

            @Override
            public void onResourceReady(
                    @NonNull Drawable resource,
                    @Nullable Transition<? super Drawable> transition
            ) {

                beforeAfterView.setResultDrawable(resource);

                beforeAfterView.playRevealAnimation();

                beforeAfterView.playCompareAnimation();
            }

            @Override
            public void onLoadCleared(
                    @Nullable Drawable placeholder
            ) {

            }
        });
```

---

# 🏗 Project Structure

```text
beforeafter/
 ├── java/
 │    └── BeforeAfterImageView.java
 │
 ├── res/
 │    ├── layout/
 │    │     └── view_before_after.xml
 │    │
 │    ├── drawable/
 │    │     ├── bg_compare_handle.xml
 │    │     ├── ic_arrow_left.xml
 │    │     └── ic_arrow_right.xml
 │    │
 │    └── values/
 │          └── attrs.xml
```

---

# 🔥 Use Cases

- AI Photo Restore
- AI Face Enhancement
- AI Upscaling
- Background Removal
- Beauty Filters
- Old Photo Restoration
- AI Editing Preview
- Before/After Sliders

---

# 🚀 Future Improvements

Planned features:
- Glide/Picasso direct support
- RTL support
- Gesture velocity/fling
- State saving
- Rounded reveal mode
- Blur edge reveal
- Compose support

---

# 🛡 Memory Safety

The library automatically:
- Cancels running animations
- Cleans up animators
- Prevents animation leaks

---

# 🤝 Contributing

Pull requests are welcome.

Feel free to:
- Improve animations
- Add Compose support
- Add new compare styles
- Improve accessibility

---

# 📄 License

MIT License

Copyright (c) JL Developers

Permission is hereby granted, free of charge,
to any person obtaining a copy of this software
and associated documentation files.

---

# 👨‍💻 Author

JL Developers
