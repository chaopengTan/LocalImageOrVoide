# LocalImageOrVoide

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
  dependencies {
	        compile 'com.github.chaopengTan:LocalImageOrVoide:v1.0.0'
	}
  
  
  Call
  
    1.AllImage
      
         LocalResource localResource = new LocalResource(MainActivity.this.getApplicationContext(),this);
         localResource.execute(LocalResource.IMAGE);
         
         Image Callback
         
              @Override
            public <T extends InfoBean> void getAlbumList(List<T> list) {
                List<ImageFolderBean> imageFolderBeen = (List<ImageFolderBean>) list;
            }
           
           
    2.AllVoide 
    
          LocalResource localResource = new LocalResource(MainActivity.this.getApplicationContext(),this);
          localResource.execute(LocalResource.VOIDE);
          
          Voide Callback
          
              @Override
            public <T extends InfoBean> void getAlbumList(List<T> list) {
                List<VideoInfo> imageFolderBeen = (List<VideoInfo>) list;
            }
