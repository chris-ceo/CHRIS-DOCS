/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at https://github.com/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Agfa Healthcare.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package chris.dicom.test;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomInputStream.IncludeBulkData;
import org.dcm4che3.io.DicomOutputStream;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Association;
import org.dcm4che3.net.Connection;
import org.dcm4che3.net.ConnectionMonitor;
import org.dcm4che3.net.Device;
import org.dcm4che3.net.Dimse;
import org.dcm4che3.net.Listener;
import org.dcm4che3.net.PDVInputStream;
import org.dcm4che3.net.Status;
import org.dcm4che3.net.TransferCapability;
import org.dcm4che3.net.pdu.PresentationContext;
import org.dcm4che3.net.service.BasicCEchoSCP;
import org.dcm4che3.net.service.BasicCStoreSCP;
import org.dcm4che3.net.service.DicomServiceException;
import org.dcm4che3.net.service.DicomServiceRegistry;
import org.dcm4che3.tool.common.CLIUtils;
import org.dcm4che3.util.AttributesFormat;
import org.dcm4che3.util.SafeClose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;

import chris.dicom.bean.InstanceBean;
import chris.dicom.util.NetworkUtil;
import chris.dicom.util.TomoConstants;

/**
 * @author Chris <chris.ceo@gmail.com>
 *
 */
public class StoreSCP  {

    private static final Logger LOG = LoggerFactory.getLogger(StoreSCP.class);

    private static ResourceBundle rb =
        ResourceBundle.getBundle("chris.dicom.props.messages");
    private static final String PART_EXT = ".part";

    private final Device device = new Device("storescp");
    private final ApplicationEntity ae = new ApplicationEntity("*");
    private final Connection conn = new Connection();
    private File storageDir;
    private AttributesFormat filePathFormat;
    private int status;
    private int[] receiveDelays;
    private int[] responseDelays;
    private int renameRetries;
    private int renameRetryJitter;
    private ConnectionMonitor cm;
    Listener ss;
    int instNumber=0;
    Gson gson = new Gson();	
    NetworkUtil nw=new NetworkUtil();

    
    private final BasicCStoreSCP cstoreSCP = new BasicCStoreSCP("*") {
    	
    	
   
        @Override
		protected void onDimseRQ(Association as, PresentationContext pc, Dimse dimse, Attributes cmd, Attributes data)
				throws IOException {
			// TODO Auto-generated method stub
        	System.out.println("Test simse RQ1");
			super.onDimseRQ(as, pc, dimse, cmd, data);
			
		}

		@Override
		public void onDimseRQ(Association as, PresentationContext pc, Dimse dimse, Attributes rq, PDVInputStream data)
				throws IOException {
			// TODO Auto-generated method stub
		//	System.out.println("Test getdimse RQ");
		//	 Attributes dataset = data.readDataset(pc.getTransferSyntax());
		//	 System.out.println(dataset.getInt(Tag.InstanceNumber,0));
			// System.out.println(s.InstanceList("Test"));
			//instNumber=dataset.getInt(Tag.InstanceNumber,0);
			super.onDimseRQ(as, pc, dimse, rq, data);
		}

		@Override
		public String[] getSOPClasses() {
			// TODO Auto-generated method stub
			System.out.println("Test set sop");
			return super.getSOPClasses();
		}

		@Override
		public void onClose(Association as) {
			// TODO Auto-generated method stub
			System.out.println("Test close");
			super.onClose(as);
		}

		@Override
        protected void store(Association as, PresentationContext pc,
                Attributes rq, PDVInputStream data, Attributes rsp)
                throws IOException {
            sleep(as, receiveDelays);
           
            try {
                rsp.setInt(Tag.Status, VR.US, status);
                if (storageDir == null)
                    return;

                String cuid = rq.getString(Tag.AffectedSOPClassUID);
                String iuid = rq.getString(Tag.AffectedSOPInstanceUID);
                System.out.println(rq);
               
                setStorageDirectory(new File("E:\\IOC Folder"+"\\"+cuid));
               
                String opt="";
                String tsuid = pc.getTransferSyntax();
                File file = File.createTempFile(iuid, PART_EXT, storageDir);
                try {
                	
                
               	
                	 Attributes attr= storeTo(as, as.createFileMetaInformation(iuid, cuid, tsuid),
                             data, file,pc);
                	
                	 InstanceBean inst=new InstanceBean();
                	 inst.setSopInstanceUid(iuid);
                	 inst.setSopClassUid(cuid);
                	 inst.setSeriesUid(attr.getString(Tag.SeriesInstanceUID));
                	 inst.setStudyUid(attr.getString(Tag.StudyInstanceUID));
                	 inst.setPatientName(attr.getString(Tag.PatientName));
                	 inst.setPatientId(attr.getString(Tag.PatientID));
                	 inst.setModality(attr.getString(Tag.Modality));	
                	 inst.setSeriesName(attr.getString(Tag.SeriesDescription));	
                	 inst.setInstanceNumber(attr.getInt(Tag.InstanceNumber,0));	
                	 
                	 try {
                	 opt= nw.postData(gson.toJson(inst),TomoConstants.STORE_INSTANCE);
                	   }catch (ConnectException e) {
       					// TODO: handle exception
                       	e.printStackTrace();
       				}
                	 
                	
                	 iuid="img_"+  opt+".dcm";
             
                	 
                     
                    renameTo(as, file, new File(storageDir,
                            filePathFormat == null
                                    ? iuid
                                    : filePathFormat.format(parse(file))));
                   // DicomInputStream in = new DicomInputStream(file);
                  //  System.out.println("Datatr " +in.readDataset().getString(Tag.PatientName));
                } catch (Exception e) {
                    deleteFile(as, file);
                    throw new DicomServiceException(Status.ProcessingFailure, e);
                    
                }
            } finally {
              System.out.println(conn.isListening());
                sleep(as, responseDelays);
          	 
            }
        }

    };

    private void sleep(Association as, int[] delays) {
  
        int responseDelay = delays != null
                ? delays[(as.getNumberOfReceived(Dimse.C_STORE_RQ) - 1) % delays.length]
                : 0;
  
        if (responseDelay > 0) {
        	
            try {
                Thread.sleep(responseDelay);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public StoreSCP() throws IOException {
    	 cm = device.getConnectionMonitor();
    	
        device.setDimseRQHandler(createServiceRegistry());
        device.addConnection(conn);
        device.addApplicationEntity(ae);
        ae.setAssociationAcceptor(true);
        ae.addConnection(conn);
       
        
    }
    

    private Attributes storeTo(Association as, Attributes fmi, 
            PDVInputStream data, File file,PresentationContext pc) throws IOException  {
    	 Attributes attr=null;
        LOG.info("{}: M-WRITE {}", as, file);
        file.getParentFile().mkdirs();
    	
    	
        DicomOutputStream out = new DicomOutputStream(file);
        
        try {
            out.writeFileMetaInformation(fmi);
            data.copyTo(out);;
            attr=parse(file);
           
            
        } finally {
            SafeClose.close(out);
        }
        return attr;
    }

    private void renameTo(Association as, File from, File dest)
            throws IOException {
        LOG.info("{}: M-RENAME {} to {}", as, from, dest);
        for(int try_count = 0; try_count <= renameRetries; try_count++) {
            try{
                dest.getParentFile().mkdirs();
                Files.move(from.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return;
            }
            catch (IOException e){
                if (try_count == renameRetries){
                    throw e;
                }
                try {
                    Thread.sleep((long)(Math.random()*renameRetryJitter));
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    private static Attributes parse(File file) throws IOException {
        DicomInputStream in = new DicomInputStream(file);
        try {
            in.setIncludeBulkData(IncludeBulkData.NO);
            
            return in.readDatasetUntilPixelData();
        } finally {
            SafeClose.close(in);
        }
    }

    private static void deleteFile(Association as, File file) {
        if (file.delete())
            LOG.info("{}: M-DELETE {}", as, file);
        else
            LOG.warn("{}: M-DELETE {} failed!", as, file);
    }

    private DicomServiceRegistry createServiceRegistry() {
        DicomServiceRegistry serviceRegistry = new DicomServiceRegistry();
        serviceRegistry.addDicomService(new BasicCEchoSCP());
        serviceRegistry.addDicomService(cstoreSCP);
        return serviceRegistry;
    }

    public void setStorageDirectory(File storageDir) {
        if (storageDir != null)
            storageDir.mkdirs();
        this.storageDir = storageDir;
    }

    public void setStorageFilePathFormat(String pattern) {
        this.filePathFormat = new AttributesFormat(pattern);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setReceiveDelays(int[] receiveDelays) {
        this.receiveDelays = receiveDelays;
    }

    public void setResponseDelays(int[] responseDelays) {
        this.responseDelays = responseDelays;
    }

    public void setRenameRetries(int renameRetries){
        if (renameRetries <0){
            throw new IllegalArgumentException("Rename retries must be a non-negative value!");
        }
        this.renameRetries = renameRetries;
    }

    public void setRenameRetryJitter(int renameRetryJitter){
        if (renameRetryJitter <0){
            throw new IllegalArgumentException("Rename retry jitter must be a non-negative value!");
        }
        this.renameRetryJitter = renameRetryJitter;
    }

    private static CommandLine parseComandLine(String[] args)
            throws ParseException {
        Options opts = new Options();
        CLIUtils.addBindServerOption(opts);
        CLIUtils.addAEOptions(opts);
        CLIUtils.addAcceptedCallingAETs(opts);
        CLIUtils.addCommonOptions(opts);
        addStatusOption(opts);
        addDelayOption(opts, "receive-delay");
        addDelayOption(opts, "response-delay");
        addStorageDirectoryOptions(opts);
        addTransferCapabilityOptions(opts);
        addRenameRetryOptions(opts);
        return CLIUtils.parseComandLine(args, opts, rb, StoreSCP.class);
    }

    private static void addStatusOption(Options opts) {
        opts.addOption(Option.builder()
                .hasArg()
                .argName("code")
                .desc(rb.getString("status"))
                .longOpt("status")
                .build());
    }

    private static void addDelayOption(Options opts, String longOpt) {
        opts.addOption(Option.builder()
                .hasArgs()
                .argName("ms")
                .desc(rb.getString(longOpt))
                .longOpt(longOpt)
                .build());
    }

    private static void addStorageDirectoryOptions(Options opts) {
        opts.addOption(null, "ignore", false,
                rb.getString("ignore"));
        opts.addOption(Option.builder()
                .hasArg()
                .argName("path")
                .desc(rb.getString("directory"))
                .longOpt("directory")
                .build());
        opts.addOption(Option.builder()
                .hasArg()
                .argName("pattern")
                .desc(rb.getString("filepath"))
                .longOpt("filepath")
                .build());
    }

    private static void addTransferCapabilityOptions(Options opts) {
        opts.addOption(null, "accept-unknown", false,
                rb.getString("accept-unknown"));
        opts.addOption(Option.builder()
                .hasArg()
                .argName("file|url")
                .desc(rb.getString("sop-classes"))
                .longOpt("sop-classes")
                .build());
    }

    private static void addRenameRetryOptions(Options opts){
        opts.addOption(Option.builder()
                .hasArg()
                .argName("count")
                .desc(rb.getString("rename-retries"))
                .longOpt("rename-retries")
                .build());
        opts.addOption(Option.builder()
                .hasArg()
                .argName("ms")
                .desc(rb.getString("rename-retry-jitter"))
                .longOpt("rename-retry-jitter")
                .build());
    }

    public static void main(String[] args) {
        try {
        	String coms[]= {"storescp", "-b", "IOC@localhost:3006","--directory","E:\\IOC Folder","status"};
            CommandLine cl = parseComandLine(coms);
            
            StoreSCP main = new StoreSCP();
            CLIUtils.configureBindServer(main.conn, main.ae, cl);
            CLIUtils.configure(main.conn, cl);
            CLIUtils.configureAcceptedCallingAETitles(main.ae, cl, LOG);
           
            main.setStatus(CLIUtils.getIntOption(cl, "status", 0));
            main.setReceiveDelays(CLIUtils.getIntsOption(cl, "receive-delay"));
            main.setResponseDelays(CLIUtils.getIntsOption(cl, "response-delay"));
            configureTransferCapability(main.ae, cl);
           configureStorageDirectory(main, cl);
            main.setRenameRetries(CLIUtils.getIntOption(cl, "rename-retries", 3));
            main.setRenameRetryJitter(CLIUtils.getIntOption(cl, "rename-retry-jitter", 50));
         
            ExecutorService executorService = Executors.newCachedThreadPool();
            ScheduledExecutorService scheduledExecutorService = 
                    Executors.newSingleThreadScheduledExecutor();
            
            main.device.setScheduledExecutor(scheduledExecutorService);
            main.device.setExecutor(executorService);
            main.device.bindConnections();
           
        } catch (ParseException e) {
            System.err.println("storescp: " + e.getMessage());
            System.err.println(rb.getString("try"));
            System.exit(2);
        } catch (Exception e) {
            System.err.println("storescp: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
    
    private static void configureStorageDirectory(StoreSCP main, CommandLine cl) {
        if (!cl.hasOption("ignore")) {
            main.setStorageDirectory(
                    new File(cl.getOptionValue("directory", ".")));
            if (cl.hasOption("filepath"))
                main.setStorageFilePathFormat(cl.getOptionValue("filepath"));
        }
    }

    private static void configureTransferCapability(ApplicationEntity ae,
            CommandLine cl) throws IOException {
        if (cl.hasOption("accept-unknown")) {
            ae.addTransferCapability(
                    new TransferCapability(null, 
                            "*",
                            TransferCapability.Role.SCP,
                            "*"));
        } else {
            Properties p = CLIUtils.loadProperties(
                    cl.getOptionValue("sop-classes", 
                            "resource:sop-classes.properties"),
                    null);
            for (String cuid : p.stringPropertyNames()) {
                String ts = p.getProperty(cuid);
                TransferCapability tc = new TransferCapability(null,
                        CLIUtils.toUID(cuid), 
                        TransferCapability.Role.SCP,
                        CLIUtils.toUIDs(ts));
                ae.addTransferCapability(tc);
            }
        }
     }

	
	



	

}