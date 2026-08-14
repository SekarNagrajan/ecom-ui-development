function updateMaskPortList(portlists) {
    if('Y'== hideMaskPortRecords){
    var maskList =maskPortList;
    
  
    
    if (Array.isArray(portlists) && maskList && Array.isArray(maskList) && maskList.length > 0) {
        
        var removedCount = 0;
        var replacedCount = 0;
        var addedCount = 0;
        
        maskList.forEach(maskPortEntry => {
            // MaskPortVO: mport = masked port code, port = actual port code
            const maskCode = maskPortEntry.mport;      // The masked port code 
            const actualCode = maskPortEntry.port;     // The actual port code 
            const actualName = maskPortEntry.portname; // The actual port name
            const maskName = maskPortEntry.mportname;  // The mask port name
            
            // Find actual port index
            const actualIndex = portlists.findIndex(p => 
                p.PortCode && p.PortCode.toUpperCase() === actualCode.toUpperCase()
            );

            // Step 1: Remove 
            if (actualIndex !== -1) {
                portlists.splice(actualIndex, 1);
                removedCount++;
            }
            
            
            const maskIndex = portlists.findIndex(p => 
                p.PortCode && p.PortCode.toUpperCase() === maskCode.toUpperCase()
            );
            
            if (maskIndex !== -1) {
                //update/replace its details
                portlists[maskIndex].PortCode = maskCode;
                portlists[maskIndex].PortName = maskName;
                replacedCount++;
            } else {
                // Step 3: add it to the list
                portlists.push({
                    PortCode: maskCode,
                    PortName: maskName
                });
                addedCount++;
            }
        });
        
        console.log("Removed:", removedCount, "Replaced:", replacedCount, "Added:", addedCount);
        console.log("Updated portlist length:", portlists.length);
        return portlists;
    }
}
    return portlists;
}

