package com.incampusit.staryaar.Filter;

/*
 * Created by PANKAJ on 3/24/2019.
 */

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.daasuu.gpuv.egl.filter.GlBilateralFilter;
import com.daasuu.gpuv.egl.filter.GlBoxBlurFilter;
import com.daasuu.gpuv.egl.filter.GlBrightnessFilter;
import com.daasuu.gpuv.egl.filter.GlBulgeDistortionFilter;
import com.daasuu.gpuv.egl.filter.GlCGAColorspaceFilter;
import com.daasuu.gpuv.egl.filter.GlContrastFilter;
import com.daasuu.gpuv.egl.filter.GlCrosshatchFilter;
import com.daasuu.gpuv.egl.filter.GlExposureFilter;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.egl.filter.GlFilterGroup;
import com.daasuu.gpuv.egl.filter.GlGammaFilter;
import com.daasuu.gpuv.egl.filter.GlGaussianBlurFilter;
import com.daasuu.gpuv.egl.filter.GlGrayScaleFilter;
import com.daasuu.gpuv.egl.filter.GlHalftoneFilter;
import com.daasuu.gpuv.egl.filter.GlHazeFilter;
import com.daasuu.gpuv.egl.filter.GlHighlightShadowFilter;
import com.daasuu.gpuv.egl.filter.GlHueFilter;
import com.daasuu.gpuv.egl.filter.GlInvertFilter;
import com.daasuu.gpuv.egl.filter.GlLuminanceFilter;
import com.daasuu.gpuv.egl.filter.GlLuminanceThresholdFilter;
import com.daasuu.gpuv.egl.filter.GlMonochromeFilter;
import com.daasuu.gpuv.egl.filter.GlOpacityFilter;
import com.daasuu.gpuv.egl.filter.GlPixelationFilter;
import com.daasuu.gpuv.egl.filter.GlPosterizeFilter;
import com.daasuu.gpuv.egl.filter.GlRGBFilter;
import com.daasuu.gpuv.egl.filter.GlSaturationFilter;
import com.daasuu.gpuv.egl.filter.GlSepiaFilter;
import com.daasuu.gpuv.egl.filter.GlSharpenFilter;
import com.daasuu.gpuv.egl.filter.GlSolarizeFilter;
import com.daasuu.gpuv.egl.filter.GlSphereRefractionFilter;
import com.daasuu.gpuv.egl.filter.GlSwirlFilter;
import com.daasuu.gpuv.egl.filter.GlToneCurveFilter;
import com.daasuu.gpuv.egl.filter.GlToneFilter;
import com.daasuu.gpuv.egl.filter.GlVibranceFilter;
import com.daasuu.gpuv.egl.filter.GlVignetteFilter;
import com.daasuu.gpuv.egl.filter.GlWeakPixelInclusionFilter;
import com.daasuu.gpuv.egl.filter.GlZoomBlurFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

// this is the all available filters
public enum FilterType {
    DEFAULT {
        @NonNull
        @Override
        public String toString() {
            return "None";
        }
    },
    BRIGHTNESS {
        @NonNull
        @Override
        public String toString() {
            return "Brightness";
        }
    },
    EXPOSURE {
        @NonNull
        @Override
        public String toString() {
            return "Exposure";
        }
    },
    FILTER_GROUP_SAMPLE {
        @NonNull
        @Override
        public String toString() {
            return "Filter Group";
        }
    },
    GAMMA {
        @NonNull
        @Override
        public String toString() {
            return "Gamma";
        }
    },
    GRAY_SCALE {
        @NonNull
        @Override
        public String toString() {
            return "Gray Scale";
        }
    },
    HAZE {
        @NonNull
        @Override
        public String toString() {
            return "Haze Tint";
        }
    },
    HIGHLIGHT_SHADOW {
        @NonNull
        @Override
        public String toString() {
            return "Shadow Effect";
        }
    },
    HUE {
        @NonNull
        @Override
        public String toString() {
            return "Hue Change";
        }
    },
    INVERT {
        @NonNull
        @Override
        public String toString() {
            return "Invert Colors";
        }
    },
    LUMINANCE_THRESHOLD {
        @NonNull
        @Override
        public String toString() {
            return "Luminuous More";
        }
    },
    LUMINANCE {
        @NonNull
        @Override
        public String toString() {
            return "Luminuous Less";
        }
    },
    MONOCHROME {
        @NonNull
        @Override
        public String toString() {
            return "Monochrome";
        }
    },
    OPACITY {
        @NonNull
        @Override
        public String toString() {
            return "Opacity";
        }
    },
    PIXELATION {
        @NonNull
        @Override
        public String toString() {
            return "Pixelate";
        }
    },
    POSTERIZE {
        @NonNull
        @Override
        public String toString() {
            return "Posterize";
        }
    },
    RGB {
        @NonNull
        @Override
        public String toString() {
            return "RGB Optimized";
        }
    },
    SATURATION {
        @NonNull
        @Override
        public String toString() {
            return "Saturated Tint";
        }
    },
    SEPIA {
        @NonNull
        @Override
        public String toString() {
            return "Sepia Mode";
        }
    },
    SHARP {
        @NonNull
        @Override
        public String toString() {
            return "Sharp Effect";
        }
    },
    BILATERAL_BLUR {
        @NonNull
        @Override
        public String toString() {
            return "Bilateral Blur";
        }
    },
    BOX_BLUR {
        @NonNull
        @Override
        public String toString() {
            return "Box Blur";
        }
    },
    BULGE_DISTORTION {
        @NonNull
        @Override
        public String toString() {
            return "Bulge Distort";
        }
    },
    CGA_COLORSPACE {
        @NonNull
        @Override
        public String toString() {
            return "CGA Color";
        }
    },
    CONTRAST {
        @NonNull
        @Override
        public String toString() {
            return "Contrast Mode";
        }
    },
    CROSSHATCH {
        @NonNull
        @Override
        public String toString() {
            return "Crosshatch Mode";
        }
    },
    GAUSSIAN_FILTER {
        @NonNull
        @Override
        public String toString() {
            return "Gaussian Mode";
        }
    },
    HALFTONE {
        @NonNull
        @Override
        public String toString() {
            return "Halftone Tint";
        }
    },
    SOLARIZE {
        @NonNull
        @Override
        public String toString() {
            return "Solarize Mode";
        }
    },
    SPHERE_REFRACTION {
        @NonNull
        @Override
        public String toString() {
            return "Sphere Refract";
        }
    },
    SWIRL {
        @NonNull
        @Override
        public String toString() {
            return "Swirl Effect";
        }
    },
    TONE_CURVE_SAMPLE {
        @NonNull
        @Override
        public String toString() {
            return "Tone Curve";
        }
    },
    TONE {
        @NonNull
        @Override
        public String toString() {
            return "Toned Tint";
        }
    },
    VIBRANCE {
        @NonNull
        @Override
        public String toString() {
            return "Vibrant Mode";
        }
    },
    VIGNETTE {
        @NonNull
        @Override
        public String toString() {
            return "Vignette Mode";
        }
    },
    WEAK_PIXEL {
        @NonNull
        @Override
        public String toString() {
            return "Weak Pixel";
        }
    },
    ZOOM_BLUR {
        @NonNull
        @Override
        public String toString() {
            return "Zoom Blur";
        }
    } /*,
    STRUCK_VIBE {
        @NonNull
        @Override
        public String toString() {
            return "Struck Vibe";
        }
    },
    CLAREDON {
        @NonNull
        @Override
        public String toString() {
            return "Claredon Tint";
        }
    },
    OLD_MAN {
        @NonNull
        @Override
        public String toString() {
            return "Old Man Tint";
        }
    },
    MARS {
        @NonNull
        @Override
        public String toString() {
            return "Mars Mode";
        }
    },
    RISE {
        @NonNull
        @Override
        public String toString() {
            return "Rise Tone";
        }
    },
    APRIL {
        @NonNull
        @Override
        public String toString() {
            return "April Tone";
        }
    },
    AMAZON{
        @NonNull
        @Override
        public String toString() {
            return "Amazon Tint";
        }
    },
    STARLIT{
        @NonNull
        @Override
        public String toString() {
            return "Starlit Hue";
        }
    },
    NIGHTWHISPER{
        @NonNull
        @Override
        public String toString() {
            return "Night Whisper";
        }
    },
    LIMESTUTTER{
        @NonNull
        @Override
        public String toString() {
            return "Lime Stutter";
        }
    },
    HAAN{
        @NonNull
        @Override
        public String toString() {
            return "Haan Mode";
        }
    },
    BLUEMESS{
        @NonNull
        @Override
        public String toString() {
            return "Bluemess Hue";
        }
    },
    ADELE{
        @NonNull
        @Override
        public String toString() {
            return "Adele Tone";
        }
    },
    CRUZ{
        @NonNull
        @Override
        public String toString() {
            return "Cruz Effect";
        }
    },
    METROPOLIS{
        @NonNull
        @Override
        public String toString() {
            return "Metropolis Hue";
        }
    },
    AUDREY{
        @NonNull
        @Override
        public String toString() {
            return "Audrey Mode";
        }
    } */;

    public static List<FilterType> createFilterList() {
        return Arrays.asList(FilterType.values());
    }

    public static GlFilter createGlFilter(FilterType filterType, Context context) {
        switch (filterType) {
            case DEFAULT:
                return new GlFilter();
            case BILATERAL_BLUR:
                return new GlBilateralFilter();
            case BOX_BLUR:
                return new GlBoxBlurFilter();
            case BRIGHTNESS:
                GlBrightnessFilter glBrightnessFilter = new GlBrightnessFilter();
                glBrightnessFilter.setBrightness(0.1f);
                return glBrightnessFilter;
            case BULGE_DISTORTION:
                return new GlBulgeDistortionFilter();
            case CGA_COLORSPACE:
                return new GlCGAColorspaceFilter();
            case CONTRAST:
                GlContrastFilter glContrastFilter = new GlContrastFilter();
                glContrastFilter.setContrast(2.5f);
                return glContrastFilter;
            case CROSSHATCH:
                return new GlCrosshatchFilter();
            case EXPOSURE:
                return new GlExposureFilter();
            case FILTER_GROUP_SAMPLE:
                return new GlFilterGroup(new GlSepiaFilter(), new GlVignetteFilter());
            case GAMMA:
                GlGammaFilter glGammaFilter = new GlGammaFilter();
                glGammaFilter.setGamma(2f);
                return glGammaFilter;
            case GAUSSIAN_FILTER:
                return new GlGaussianBlurFilter();
            case GRAY_SCALE:
                return new GlGrayScaleFilter();
            case HALFTONE:
                return new GlHalftoneFilter();
            case HAZE:
                GlHazeFilter glHazeFilter = new GlHazeFilter();
                glHazeFilter.setSlope(-0.5f);
                return glHazeFilter;
            case HIGHLIGHT_SHADOW:
                return new GlHighlightShadowFilter();
            case HUE:
                return new GlHueFilter();
            case INVERT:
                return new GlInvertFilter();
            case LUMINANCE:
                return new GlLuminanceFilter();
            case LUMINANCE_THRESHOLD:
                return new GlLuminanceThresholdFilter();
            case MONOCHROME:
                return new GlMonochromeFilter();
            case OPACITY:
                return new GlOpacityFilter();
            case PIXELATION:
                return new GlPixelationFilter();
            case POSTERIZE:
                return new GlPosterizeFilter();
            case RGB:
                GlRGBFilter glRGBFilter = new GlRGBFilter();
                glRGBFilter.setRed(0f);
                return glRGBFilter;
            case SATURATION:
                return new GlSaturationFilter();
            case SEPIA:
                return new GlSepiaFilter();
            case SHARP:
                GlSharpenFilter glSharpenFilter = new GlSharpenFilter();
                glSharpenFilter.setSharpness(3f);
                return glSharpenFilter;
            case SOLARIZE:
                return new GlSolarizeFilter();
            case SPHERE_REFRACTION:
                return new GlSphereRefractionFilter();
            case SWIRL:
                return new GlSwirlFilter();
            case TONE_CURVE_SAMPLE:
                try {
                    InputStream is = context.getAssets().open("acv/tone_cuver_sample.acv");
                    return new GlToneCurveFilter(is);
                } catch (IOException e) {
                    Log.e("FilterType", "Error");
                }
                return new GlFilter();
            case TONE:
                return new GlToneFilter();

            case VIBRANCE:
                GlVibranceFilter glVibranceFilter = new GlVibranceFilter();
                glVibranceFilter.setVibrance(3f);
                return glVibranceFilter;

            case VIGNETTE:
                return new GlVignetteFilter();

            case WEAK_PIXEL:
                return new GlWeakPixelInclusionFilter();

            case ZOOM_BLUR:
                return new GlZoomBlurFilter();

            default:
                return new GlFilter();
        }
    }


}