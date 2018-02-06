package client;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

/**
 * Created by likun on 2018/2/6 0006.
 */
public class ClientTest {
    public static void main(String[] args){
        try {
            // axis2 服务端
            String url = "http://localhost:8081/services/helloWorld?wsdl";

            // 使用RPC方式调用WebService
            RPCServiceClient serviceClient = new RPCServiceClient();
            // 指定调用WebService的URL
            EndpointReference targetEPR = new EndpointReference(url);
            Options options = serviceClient.getOptions();
            //确定目标服务地址
            options.setTo(targetEPR);
            //确定调用方法
            options.setAction("urn:sayHelloWorldFrom");

            /**
             * 指定要调用的getPrice方法及WSDL文件的命名空间
             * 如果 webservice 服务端由axis2编写
             * 命名空间 不一致导致的问题
             * org.apache.axis2.AxisFault: java.lang.RuntimeException: Unexpected subelement arg0
             */
            QName qname = new QName("http://example", "sayHelloWorldFrom");
            // 指定getPrice方法的参数值
            Object[] parameters = new Object[] { "axis2_test" };

            // 指定getPrice方法返回值的数据类型的Class对象
            Class[] returnTypes = new Class[] { String.class };

            // 调用方法一 传递参数，调用服务，获取服务返回结果集
            OMElement element = serviceClient.invokeBlocking(qname, parameters);
            //值得注意的是，返回结果就是一段由OMElement对象封装的xml字符串。
            //我们可以对之灵活应用,下面我取第一个元素值，并打印之。因为调用的方法返回一个结果
            String result = element.getFirstElement().getText();
            System.out.println(result);

            // 调用方法二 getPrice方法并输出该方法的返回值
            Object[] response = serviceClient.invokeBlocking(qname, parameters, returnTypes);
             String r = (String) response[0];
            System.out.println(r);

        } catch (AxisFault e) {
            e.printStackTrace();
        }
    }
}
