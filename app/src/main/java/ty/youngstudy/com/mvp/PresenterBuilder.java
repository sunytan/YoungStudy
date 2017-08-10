package ty.youngstudy.com.mvp;

/**
 * Created by edz on 2017/8/8.
 */

public class PresenterBuilder {

    public static <PresenterType extends Presenter> PresenterType fromViewClass(Class<?> viewClass) {
        RequirePresenter annotation = viewClass.getAnnotation(RequirePresenter.class);
        if (annotation == null) {
            return null;
        }
        Class<PresenterType> presenterClass = (Class<PresenterType>)annotation.value();
        PresenterType mPresenter;
        try {
            mPresenter = presenterClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return mPresenter;
    }
}
