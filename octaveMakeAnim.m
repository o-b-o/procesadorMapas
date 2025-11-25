% --- Create an Animation ---
figure; % Create a new window for the animation

startWavenumber = 900;
stepWavenumber = 10;
numSlices = size(volumetricData, 3); % Get the number of slices (e.g., 91)

for k = 1:numSlices
    % Display the k-th slice
    imagesc(volumetricData(:,:,k));
    
    % Calculate the approximate wavenumber for this slice
    currentWavenumber = startWavenumber + (k-1) * stepWavenumber;
    
    % Update the title on each frame
    title(sprintf('Slice %d / %d (Wavenumber ~%.0f cm^{-1})', k, numSlices, currentWavenumber));
    
    colorbar;
    axis equal tight;
    
    % This command forces Octave to draw the plot now
    drawnow;
end
