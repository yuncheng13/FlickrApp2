An Android app using the Flickr API which displays recent photos. 
Features:
- User can search for photos by keyword or view recent photos in a grid view by default.
- Loading UI: spinning circular progress bar
- Error handling: shows an error state with a retry button to reload photos
- Pagination: manual implementation listens for the end of current list of photos and will load the next page of photos
- Unit tests for PhotosViewModel
  
```
app/
 ├── data/
 │    ├── model/
 │    │    ├── FlickrPhoto
 │    │    ├── FlickrResponse
 │    │    ├── PhotoPage
 │    └── remote/
 │    │    ├── FlickrApi
 │    │    ├── FlickrRepository
 ├── ui/
 │    ├── theme/
 │    ├── PhotosViewModel.kt
 └── MainActivity.kt
```
