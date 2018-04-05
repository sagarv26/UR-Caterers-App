package com.bumptech.glide.load.model;

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import java.util.HashMap;
import java.util.Map;

public class GenericLoaderFactory {
    private static final ModelLoader NULL_MODEL_LOADER = new C04331();
    private final Map<Class, Map<Class, ModelLoader>> cachedModelLoaders = new HashMap();
    private final Context context;
    private final Map<Class, Map<Class, ModelLoaderFactory>> modelClassToResourceFactories = new HashMap();

    static class C04331 implements ModelLoader {
        C04331() {
        }

        public DataFetcher getResourceFetcher(Object model, int width, int height) {
            throw new NoSuchMethodError("This should never be called!");
        }

        public String toString() {
            return "NULL_MODEL_LOADER";
        }
    }

    public GenericLoaderFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    public synchronized <T, Y> ModelLoaderFactory<T, Y> unregister(Class<T> modelClass, Class<Y> resourceClass) {
        ModelLoaderFactory result;
        this.cachedModelLoaders.clear();
        result = null;
        Map<Class, ModelLoaderFactory> resourceToFactories = (Map) this.modelClassToResourceFactories.get(modelClass);
        if (resourceToFactories != null) {
            result = (ModelLoaderFactory) resourceToFactories.remove(resourceClass);
        }
        return result;
    }

    public synchronized <T, Y> ModelLoaderFactory<T, Y> register(Class<T> modelClass, Class<Y> resourceClass, ModelLoaderFactory<T, Y> factory) {
        ModelLoaderFactory previous;
        this.cachedModelLoaders.clear();
        Map<Class, ModelLoaderFactory> resourceToFactories = (Map) this.modelClassToResourceFactories.get(modelClass);
        if (resourceToFactories == null) {
            resourceToFactories = new HashMap();
            this.modelClassToResourceFactories.put(modelClass, resourceToFactories);
        }
        previous = (ModelLoaderFactory) resourceToFactories.put(resourceClass, factory);
        if (previous != null) {
            for (Map<Class, ModelLoaderFactory> factories : this.modelClassToResourceFactories.values()) {
                if (factories.containsValue(previous)) {
                    previous = null;
                    break;
                }
            }
        }
        return previous;
    }

    @Deprecated
    public synchronized <T, Y> ModelLoader<T, Y> buildModelLoader(Class<T> modelClass, Class<Y> resourceClass, Context context) {
        return buildModelLoader(modelClass, resourceClass);
    }

    public synchronized <T, Y> ModelLoader<T, Y> buildModelLoader(Class<T> modelClass, Class<Y> resourceClass) {
        ModelLoader<T, Y> modelLoader;
        ModelLoader<T, Y> result = getCachedLoader(modelClass, resourceClass);
        if (result == null) {
            ModelLoaderFactory<T, Y> factory = getFactory(modelClass, resourceClass);
            if (factory != null) {
                result = factory.build(this.context, this);
                cacheModelLoader(modelClass, resourceClass, result);
            } else {
                cacheNullLoader(modelClass, resourceClass);
            }
            modelLoader = result;
        } else if (NULL_MODEL_LOADER.equals(result)) {
            modelLoader = null;
        } else {
            modelLoader = result;
        }
        return modelLoader;
    }

    private <T, Y> void cacheNullLoader(Class<T> modelClass, Class<Y> resourceClass) {
        cacheModelLoader(modelClass, resourceClass, NULL_MODEL_LOADER);
    }

    private <T, Y> void cacheModelLoader(Class<T> modelClass, Class<Y> resourceClass, ModelLoader<T, Y> modelLoader) {
        Map<Class, ModelLoader> resourceToLoaders = (Map) this.cachedModelLoaders.get(modelClass);
        if (resourceToLoaders == null) {
            resourceToLoaders = new HashMap();
            this.cachedModelLoaders.put(modelClass, resourceToLoaders);
        }
        resourceToLoaders.put(resourceClass, modelLoader);
    }

    private <T, Y> ModelLoader<T, Y> getCachedLoader(Class<T> modelClass, Class<Y> resourceClass) {
        Map<Class, ModelLoader> resourceToLoaders = (Map) this.cachedModelLoaders.get(modelClass);
        if (resourceToLoaders != null) {
            return (ModelLoader) resourceToLoaders.get(resourceClass);
        }
        return null;
    }

    private <T, Y> ModelLoaderFactory<T, Y> getFactory(Class<T> modelClass, Class<Y> resourceClass) {
        Map<Class, ModelLoaderFactory> resourceToFactories = (Map) this.modelClassToResourceFactories.get(modelClass);
        ModelLoaderFactory modelLoaderFactory = null;
        if (resourceToFactories != null) {
            modelLoaderFactory = (ModelLoaderFactory) resourceToFactories.get(resourceClass);
        }
        if (modelLoaderFactory == null) {
            for (Class<? super T> registeredModelClass : this.modelClassToResourceFactories.keySet()) {
                if (registeredModelClass.isAssignableFrom(modelClass)) {
                    Map<Class, ModelLoaderFactory> currentResourceToFactories = (Map) this.modelClassToResourceFactories.get(registeredModelClass);
                    if (currentResourceToFactories != null) {
                        modelLoaderFactory = (ModelLoaderFactory) currentResourceToFactories.get(resourceClass);
                        if (modelLoaderFactory != null) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return modelLoaderFactory;
    }
}
