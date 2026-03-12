# Surblime

An Android library that provides a complete architectural foundation and reusable components for building data-driven Android applications.

## Features

- **MVVM Architecture** — Base classes for Models, ViewModels, and data-bound Views with observable state management (LOADING, SUCCESS, ERROR)
- **API Layer** — Retrofit + RxJava 2 integration with built-in HTTP response caching, query composition, and interceptor support
- **Form System** — Declarative form fields (text, number, boolean, spinner, image) with built-in validation and error display
- **Pagination** — Built-in support for paginated lists via `PagedObservableRecyclerViewModel`
- **Image Management** — Cloudinary integration for cloud-based image uploads, plus Picasso for local/remote image loading
- **Network Awareness** — Automatic detection of connectivity changes with EventBus, triggering UI refresh on reconnect
- **Custom Widgets** — Loading buttons, image input fields, form field layouts, custom tab layouts, and more
- **Pull-to-Refresh** — SwipeRefreshLayout integration across list and detail fragments
- **Permission Handling** — Runtime permission management baked into base activities
- **Custom Fonts** — Calligraphy integration with bundled Josefin Sans and Quicksand fonts

## Requirements

- **Min SDK:** 19 (Android 4.4 KitKat)
- **Target SDK:** 28 (Android 9 Pie)
- **Java:** 8

## Project Structure

```
src/main/java/com/mg/surblime/
├── activities/      Base activity classes (drawer, toolbar, swipe-back)
├── api/             Retrofit API clients, queries, and caching
├── database/        ORM Lite table utilities
├── events/          EventBus events (network state)
├── forms/           Form fields, validators, and form context
├── tasks/           AsyncTasks for downloading/sharing images
├── ui/              Base fragments (resource, model, list, paged list, form)
├── util/            File I/O, MD5 hashing, image transforms
├── widgets/         Custom UI components
├── BaseModel.java
├── BaseViewModel.java
├── ObservableViewModel.java
├── ObservableRecyclerViewModel.java
└── SurblimeBindingAdapters.java
```

## Key Dependencies

| Library | Purpose |
|---------|---------|
| Retrofit 2.5 | HTTP client |
| RxJava 2 / RxAndroid | Reactive programming |
| ORM Lite 5.0 | Local database |
| Picasso 2.5 | Image loading |
| Cloudinary 1.25 | Cloud image uploads |
| EventBus 3.1 | Event communication |
| Calligraphy 2.3 | Custom fonts |
| Gson 2.8 | JSON serialization |

## Usage

### ViewModel

Extend `ObservableRecyclerViewModel` for lists or `SingleViewModel` for single items. The base classes handle loading states and refresh listeners automatically.

### API Queries

Subclass `SurblimeAPI` to configure your Retrofit service, then use `SimpleQuery`, `ListQuery`, or `SingleItemQuery` to make API calls with automatic caching.

### Forms

Create a `FormContext` with typed fields, attach validators, and bind to your layout via data binding. The form system handles validation, error display, and submission.

### Fragments

Use the provided base fragments to quickly scaffold screens:
- `ModelFragment` — single resource with swipe-to-refresh
- `ModelListFragment` — RecyclerView list
- `PagedModelListFragment` — paginated list
- `FormFragment` / `ImageFormFragment` — form submission

## Author

Moses Gitau

## License

Copyright © 2018
