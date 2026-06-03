package tw.edu.fju.miniclinic.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String path = request.getServletPath();

        // 1. 放行登入相關路徑、靜態資源與錯誤頁面，避免無窮重導迴圈
        if (path.equals("/login") || path.equals("/logout") || path.equals("/error") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            return true; 
        }

        HttpSession session = request.getSession();
        Object loggedIn = session.getAttribute("loggedInDoctorId");

        if (loggedIn == null) {
            // 未登入：API 請求回 401，頁面請求重導到 /login
            if (path.startsWith("/api/")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401
                response.getWriter().write("{\"error\":\"請先登入\"}");
            } else {
                response.sendRedirect("/login");
            }
            return false;  // 阻止後續執行
        }

        return true;  // 放行
    }
}
