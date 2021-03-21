package contactassigment.contactlistapp.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({NoSuchIndexException.class})
	protected ModelAndView handleConflicts(RuntimeException ex,
			HttpServletRequest request) {

		String reason = "ElasticSearch store isn't available.";
		String exMessage = ex.getCause()
				.getLocalizedMessage();

		ApiError apiError = ApiError.builder()
				.reason(reason)
				.causeMessage(exMessage)
				.requestUrl(request.getRequestURL().toString())
				.build();

		ModelAndView mav = new ModelAndView();
		mav.addObject("appException", apiError);
		mav.addObject("url", request.getContextPath());
		mav.setViewName("/error");

		return mav;

	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		logger.error("Request: " + req.getRequestURL() + " raised " + ex);
		ModelAndView mav = buildErrorModelView(req, ex);
		return mav;
	}

	private static ModelAndView buildErrorModelView(HttpServletRequest req,
			Exception ex) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", ex);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("/error");
		return mav;
	}
}
