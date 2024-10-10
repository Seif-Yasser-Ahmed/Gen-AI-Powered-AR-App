# Gen-AI-Powered-AR-App

Welcome to the **Gen-AI-Powered-AR-App** repository! This project explores various architectures for generating 3D images from 2D images and implements a text-to-image generation model. It also includes a Kotlin application that leverages the **Meshy API** to visualize 3D models in augmented reality using ARCore.

After extensive experimentation with different models and techniques, we have reached a final architecture that is almost working as expected, with promising results shown in the last day of development. These results are discussed in detail in the final section of this repository.

# Team Members

> - [Abdallah Essam El-Sakka](https://github.com/al-sakka)
> - [Mohamed Ahmed Esmat](https://github.com/Mohamed-Ahmed-Esmat)
> - [Mohamed Hany Fadel](https://github.com/Mohamed-Fadel222)
> - [Seif Yasser Ahmed](https://github.com/Seif-Yasser-Ahmed) 
> - [Saifeldin Mohamed Hatem](https://github.com/Trimbex)
> - [Youssef Mahmoud hassan](https://github.com/youssef123tt)

## Table of Contents

- [Project Overview](#project-overview)
- [Datasets](#datasets)
- [Notebooks](#notebooks)
- [Kotlin Application](#kotlin-application)
- [Installation](#installation)
- [Acknowledgments](#acknowledgments)
- [License](#license)

## Project Overview

This repository contains several Jupyter notebooks showcasing the following:

1. **3D Image Generation**: Implementation of various architectures for converting 2D images into 3D images using the **Pix3D dataset**.
2. **Text-to-Image Generation**: Techniques for generating images from text descriptions using the **CUB200-2011 dataset**.
3. **AR Visualization**: A Kotlin application that utilizes the **Meshy API** to render 3D models in augmented reality based on user prompts.

## Datasets

- [**Pix3D Dataset**](http://pix3d.csail.mit.edu/): This dataset is used for training models to generate 3D images from 2D images.
- [**CUB200-2011 Dataset**](https://paperswithcode.com/dataset/cub-200-2011): A dataset for text-to-image generation, containing images of birds with corresponding textual descriptions.

## Notebooks

The following notebooks are included in this repository:
1. [`3d-Pix3pix.ipynb`](Notebooks/3DPix2Pix/3d-pix3pix.ipynb): Implementatation and training of 3D-Pix2Pix with a U-Net Generator and a Patch Discriminator.
2. [`Pix3Pix.ipynb`](Notebooks/3DPix2Pix/Pix3Pix.ipynb): Implementation and training of the final version of 3D-Pix2Pix that is working as well as results
3. [`image2vox-model.ipynb`](Notebooks/Pix2Vox/image2vox-model.ipynb): Implementation and training of Pix2Vox.
4. [`Pix2Vox-Pretrained-A.ipynb`](Notebooks/Pix2Vox/Pix2Vox-Pretrained-A.ipynb): Inference of the pretrained `Pix2Vox-A` version.
5. [`pretrained-pix2vox-F.ipynb`](Notebooks/Pix2Vox/pretrained-pix2vox-F.ipynb): Inference of the pretrained `Pix2Vox-F` version.
6. [`dcgan-cls.ipynb`](Notebooks/Text2Image/dcgan-cls.ipynb): Implementation of a Text-To-Image cGAN, leveraging text descriptions as conditioning inputs to generate corresponding images.
7. [`dcgan-cls_one_Cat.ipynb`](Notebooks/Text2Image/dcgan-cls_one_Cat.ipynb): Implementation of a Text-To-Image cGAN on one category of images due to lack of resources.
8. [`Notebooks/PIFuHD/`](Notebooks/PIFuHD): Exploring PIFuHD from [Meta Research](https://github.com/facebookresearch) for High-Resolution 3D Human Digitization.
9. [`mesh-reconstruction-pytorch3d.ipynb`](Notebooks/Mesh-Reconstruction/mesh-reconstruction-pytorch3d.ipynb): Exploring Pytorch3D from [Meta Research](https://github.com/facebookresearch).

## Kotlin Application

The Kotlin app provides an interface for users to input prompts, which are processed to visualize a 3D model using the Meshy API and ARCore. This application enhances user interaction by allowing them to see generated 3D models in an augmented reality environment.

### Key Features:

- User-friendly interface for inputting prompts.
- Real-time visualization of 3D models in AR.
### Running the Application
- Open the Kotlin project in your preferred IDE.
- Ensure the Meshy API is correctly set up and configured.
- Run the application and follow the instructions to visualize 3D models in AR.

## Installation

To get started with the project, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/Seif-Yasser-Ahmed/Gen-AI-Powered-AR-App.git
   ```
2. Navigate to the project directory:
    ```bash
    cd Gen-AI-Powered-AR-App
    ```

3. Install the required Python packages:
    ```bash
    pip install -r requirements.txt
    ```
4. Install Android Studio
   [Android Studio](https://developer.android.com/)

## Acknowledgments
We would like to express our gratitude to the following repositories and their contributors for their valuable resources:

PiFuHD by MetaResearch for providing the framework and models used in this project.
ICML 2016 Text-to-Image Generation for inspiring methodologies in text-to-image generation.
Pix2Vox for its implementation and pretrained models, which contributed to our 3D image generation efforts.

## License
This project is licensed under the MIT [`LISENCE`](LICENSE). See the LICENSE file for more details.
